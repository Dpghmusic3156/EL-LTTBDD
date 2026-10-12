package com.example.baitap2_3

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.baitap2_3.ui.theme.BaiTap2_3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTap2_3Theme {
                    BaiTap2_3(
                    )
                }
            }
        }
    }

class Student (
    var name: String = "",
    var score: Double?=null
)

@Composable
fun BaiTap2_3()
{
    var dsDiem = listOf(
        Student("Nguyễn Văn An",9.0),
        Student("Trần Thị Bình",7.2),
        Student("Lê Minh Chi",null),
        Student("Phạm Quốc Dũng",4.5),
        Student("Võ Thanh EM",5.8)
    )
    var tongDiem =0.0
    var soLuongCoDiem =0
    var soSinhVienGioi=0
    var max=0.0
    for (x in dsDiem)
    {
        if(x.score != null)
        {
            x.score?.let { diem ->
                tongDiem +=diem
                soLuongCoDiem++
                if (max<diem)
                {
                    max=diem
                }
            }
            var bo = false
            x.score?.let {diem ->
                bo =diem>= 8.5
            }
            if(bo)
            {
                soSinhVienGioi++
            }

        }
    }
    Column(modifier = Modifier.fillMaxSize().background(color = Color.LightGray).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth().background(color = Color(0xFF223E6E)).padding(start = 20.dp, top = 50.dp, bottom = 20.dp)) {
            Text("Danh sách điểm", fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)
        }
        Column(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp).background(color = Color(0xFF223E6E), shape = RoundedCornerShape(10.dp)).padding(16.dp)) {
            Text("Điểm trung bình lớp",color = Color.White)
            Text("${tongDiem/soLuongCoDiem}", fontSize = 30.sp,fontWeight = FontWeight.Bold,color = Color.White)
            HorizontalDivider(color = Color.White)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Text("Giỏi",color = Color.White)
                    Text("${soSinhVienGioi}",color = Color.White)
                }
                Column() {
                    Text("Chưa có điểm",color = Color.White)
                    Text("${dsDiem.size-soLuongCoDiem}",color = Color.White)
                }
                Column() {
                    Text("Cao nhất",color = Color.White)
                    Text("${max}",color = Color.White)
                }
            }
        }
        Text("DANH SÁCH - ${dsDiem.size} SINH VIÊN", Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            for (x in dsDiem)
            {
                var xephang = if(x.score != null )
                {
                    when
                    {
                        x.score!! >= 8.5 ->"Giỏi"
                        x.score!! >= 7.0 ->"Khá"
                        x.score!! >= 5.0 ->"Trung bình"
                        else -> "Yếu"

                    }
                }
                else
                {
                    "Chưa có điểm"
                }
                var mau = if(x.score != null )
                {
                    when
                    {
                        x.score!! >= 8.5 -> Color(0xFF4CAF50)
                        x.score!! >= 7.0 -> Color(0xFF4865AD)
                        x.score!! >= 5.0 -> Color(0xFFAD7d54)
                        else -> Color.Red

                    }
                }
                else
                {
                    Color.Gray
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp).background(color = Color.White, shape = RoundedCornerShape(10.dp)).padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Column() {
                        Text("${x.name}", fontWeight = FontWeight.Bold)
                        Text(xephang, color = mau)
                    }
                    if (x.score != null)
                    {
                        Text("${x.score}", fontWeight = FontWeight.Bold)
                    }
                    else
                    {
                        Text("--")
                    }
                }
            }

        }


    }

}