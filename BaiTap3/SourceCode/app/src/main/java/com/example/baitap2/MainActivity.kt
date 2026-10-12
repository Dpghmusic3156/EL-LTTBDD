package com.example.baitap2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Card
import androidx.compose.ui.unit.sp
import com.example.baitap2_1.ui.theme.BaiTap2_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap2_1Theme {
                    BaiTap2_1(
                    )
                }
            }
        }
    }

class Student(
    var name: String = "",
    var email: String? = null,
    var phone: String?=null
)


@Composable
fun BaiTap2_1() {
    val students = listOf(
        Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
        Student("Trần Thị Bình", null, "0912 888 999"),
        Student("Lê Minh Chi", "chi@sv.edu.vn", null),
        Student("Phạm Quốc Dũng", null, null),
        Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
    )
    Column() {
        Row(modifier = Modifier.fillMaxWidth().background(color = Color(0xFF2E4476)).padding(
            start = 20.dp,
            top = 50.dp,
            bottom = 30.dp
        )) {
            Text("Danh sách sinh viên", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        }
        for (x in students)
        {
            Card(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("${x.name}", fontWeight = FontWeight.Bold)
                    Row {
                        Text("email:")
                        Text("${x.email?:"Chưa cập nhật"}", color = if(x.email==null)
                        {
                            Color.Red
                        }
                        else
                        {
                            Color.Gray
                        })
                    }
                    Row {
                        Text("SĐT:")
                        Text("${x.phone?:"Chưa cập nhật"}", color = if(x.phone==null)
                        {
                            Color.Red
                        }
                        else
                        {
                            Color.Gray
                        })
                    }
                }
            }

        }
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row {
                Text("Tổng:")
                Text("${students.size} sinh viên", fontWeight = FontWeight.Bold)
            }
            Row {
                Text("Thiếu email :")
                Text("${students.count({it.email==null})}", color = Color.Red)
            }
        }
    }
}