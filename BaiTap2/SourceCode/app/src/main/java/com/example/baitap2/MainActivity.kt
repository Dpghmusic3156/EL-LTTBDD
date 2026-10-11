package com.example.baitap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.baitap2.ui.theme.BaiTap2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap2Theme {
                    BaiTap2(
                    )
                }
            }
        }
    }


@Composable
fun BaiTap2()
{
    var Num1 by remember {
        mutableStateOf("")
    }
    var Num2 by remember {
        mutableStateOf("")
    }
    var ketqua by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        Text("Thực Hành 03", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = Num1,
            onValueChange = {Num1=it},
            label = {Text("Nhập số thứ nhất")},
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(color = Color.Black)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            Button(onClick = {
                ketqua = tinh(Num1, Num2, "+")
            }, shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )) {
                Text("+")
            }
            Button(onClick = {
                ketqua = tinh(Num1, Num2, "-")
            }, shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDDAA44),
                    contentColor = Color.Black
                )) {
                Text("-")
            }
            Button(onClick = {
                ketqua = tinh(Num1, Num2, "*")
            }, shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5B3FD9),
                    contentColor = Color.White
                )) {
                Text("*")
            }
            Button(onClick = {
                ketqua = tinh(Num1, Num2, "/")
            }, shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )) {
                Text("/")
            }

        }
        OutlinedTextField(
            value = Num2,
            onValueChange = {Num2=it},
            label = {Text("Nhập số thứ 2")},
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(color = Color.Black)
        )
        Text("Kết quả : $ketqua")
    }
}

fun tinh(so1: String, so2: String, phepToan: String): String {
    val a = so1.toDoubleOrNull()
    val b = so2.toDoubleOrNull()
    if (a == null || b == null) {
        return "Vui lòng nhập số hợp lệ"
    } else if (phepToan == "+") {
        return (a + b).toString()
    } else if (phepToan == "-") {
        return (a - b).toString()
    } else if (phepToan == "*") {
        return (a * b).toString()
    } else if (phepToan == "/") {
        if (b == 0.0) { return "Không thể chia cho 0" }
        return (a / b).toString()
    } else {
        return ""
    }
}
