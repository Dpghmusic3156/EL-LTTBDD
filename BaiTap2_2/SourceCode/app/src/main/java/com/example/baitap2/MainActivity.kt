package com.example.baitap2_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.baitap2_2.ui.theme.BaiTap2_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap2_2Theme {
                    BaiTap2_2(
                    )
                }
            }
        }
    }

@Composable
fun BaiTap2_2()
{
    val dsDiem = listOf(9.0,7.0,5.5,3.0,11.0)
    Column(modifier = Modifier.fillMaxSize().background(color = Color.LightGray)) {
        Row(modifier = Modifier.fillMaxWidth().background(color = Color.Blue).padding(
            start = 20.dp,
            top = 50.dp,
            bottom = 20.dp
        )) {
            Text("Xếp loại học lực", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = Color.White,
            shape = RoundedCornerShape(8.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Điểm trung bình")
            Text("8.5", fontWeight = FontWeight.Bold, fontSize = 50.sp)
            Text("Giỏi", modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = Color.Green,
                shape = RoundedCornerShape(10.dp)).padding(16.dp),
                textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Blue)
        }
        Text("KIỂM TRA THÊM", Modifier.padding(20.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).background(color = Color.White,
            shape = RoundedCornerShape(8.dp)).padding(16.dp)) {
            for (x in dsDiem)
            {
                val p = when
                {
                    x>10 -> "Không hợp lệ"
                    x>=8.5 ->"Giỏi"
                    x>= 7 -> "Khá"
                    x>= 5 -> "Trung bình"
                    else -> "Yếu"
                }
                val bgColor = when
                {
                    x>10 -> Color.LightGray
                    x>=8.5 -> Color.Blue
                    x>= 7 -> Color.Cyan
                    x>= 5 -> Color.Red
                    else -> Color.Yellow
                }
                val textColor   = when
                {
                    x>10 -> Color.White
                    x>=8.5 -> Color.Green
                    x>= 7 -> Color.Black
                    x>= 5 -> Color.Blue
                    else -> Color.Red
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Text("Điểm ${x}",Modifier.padding(16.dp), fontSize = 20.sp)
                    }
                    Row {
                        Text(p, Modifier.padding(8.dp).background(color = bgColor,
                            shape = RoundedCornerShape(8.dp)).padding(8.dp), color = textColor)
                    }

                }
                HorizontalDivider()
            }

        }
    }
}