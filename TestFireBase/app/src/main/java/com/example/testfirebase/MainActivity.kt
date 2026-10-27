package com.example.testfirebase

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.testfirebase.ui.theme.TestFireBaseTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestFireBaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GmailLoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    data class Success(val email: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

private fun getWebClientId(context: Context): String {
    val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
    return if (resId != 0) context.getString(resId) else ""
}

@Composable
fun GmailLoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var authState by remember { mutableStateOf<AuthState>(AuthState.Idle) }

    // Google Sign-In Activity Launcher (Fallback method)
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnSuccessListener { authResult ->
                            val email = authResult.user?.email ?: "sample@gmail.com"
                            authState = AuthState.Success(email)
                        }
                        .addOnFailureListener { e ->
                            authState = AuthState.Error(e.localizedMessage ?: "Google Sign-In Failed")
                        }
                } else {
                    authState = AuthState.Error("Google Sign-In Failed: No ID Token retrieved")
                }
            } catch (e: ApiException) {
                if (e.statusCode == 12501 || e.statusCode == 16) {
                    authState = AuthState.Error("User canceled the Google sign-in process.")
                } else {
                    authState = AuthState.Error(e.localizedMessage ?: "Google Sign-In Failed")
                }
            }
        } else {
            authState = AuthState.Error("User canceled the Google sign-in process.")
        }
    }

    // Main Google Sign-In Trigger
    fun performGoogleSignIn() {
        val webClientId = getWebClientId(context)
        
        // Try Credential Manager API first (Android 14+)
        coroutineScope.launch {
            try {
                if (webClientId.isNotEmpty()) {
                    val credentialManager = CredentialManager.create(context)
                    val rawNonce = UUID.randomUUID().toString()
                    val bytes = MessageDigest.getInstance("SHA-256").digest(rawNonce.toByteArray())
                    val nonce = bytes.fold("") { str, it -> str + "%02x".format(it) }

                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(webClientId)
                        .setNonce(nonce)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(context, request)
                    val credential = result.credential
                    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        
                        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                            .addOnSuccessListener { authResult ->
                                val email = authResult.user?.email ?: "sample@gmail.com"
                                authState = AuthState.Success(email)
                            }
                            .addOnFailureListener { e ->
                                authState = AuthState.Error(e.localizedMessage ?: "Google Sign-In Failed")
                            }
                        return@launch
                    }
                }
            } catch (_: GetCredentialCancellationException) {
                authState = AuthState.Error("User canceled the Google sign-in process.")
                return@launch
            } catch (_: Exception) {
                // Fallback to legacy GoogleSignInClient below
            }

            // Fallback: Legacy GoogleSignInClient
            try {
                val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                
                if (webClientId.isNotEmpty()) {
                    gsoBuilder.requestIdToken(webClientId)
                }

                val gso = gsoBuilder.build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            } catch (e: Exception) {
                authState = AuthState.Error(e.localizedMessage ?: "Google Sign-In Failed")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Main Login Button
        Button(
            onClick = {
                // Sign out previous session if any to trigger account chooser
                FirebaseAuth.getInstance().signOut()
                performGoogleSignIn()
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) // Bright blue
            )
        ) {
            Text(
                text = "Login by Gmail",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State-based Output Cards
        when (val state = authState) {
            is AuthState.Idle -> {
                // Initial state - empty space below button
            }
            is AuthState.Error -> {
                // Error Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(
                            color = Color(0xFFFDE8E8), // Light red container
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Google Sign-In Failed",
                            color = Color(0xFF222222),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = state.message,
                            color = Color(0xFF555555),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            is AuthState.Success -> {
                // Success Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(
                            color = Color(0xFFE1F5FE), // Light blue container
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Success!",
                            color = Color(0xFF222222),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Hi ${state.email}",
                            color = Color(0xFF222222),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("Welcome to ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("UTHSmartTasks")
                                }
                            },
                            color = Color(0xFF333333),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}