package com.mesh.voda.presentation.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VodaBgColor = Color(0xFFFBF9F1)
private val VodaCardBgColor = Color(0xFFFFFFFF)
private val VodaGreenMain = Color(0xFF539145)
private val VodaHeartColor = Color(0xFFE55353)
private val VodaLineColor = Color(0xFFEDE8DC) // 💡 요구사항: 테두리 컬러 #EDE8DC 반영

data class SavedVolunteer(
    val id: Int,
    val emoji: String,
    val emojiBg: Color,
    val category: String,
    val categoryColor: Color,
    val categoryBg: Color,
    val dDay: String? = null,
    val title: String,
    val locationAndTime: String
)

@Composable
fun SavedScreen() {
    var savedList by remember {
        mutableStateOf(
            listOf(
                SavedVolunteer(
                    id = 1, "👵", Color(0xFFFFE8E8),
                    "노인·어르신", Color(0xFFE55381), Color(0xFFFFEBF0),
                    title = "노인복지관 말벗 봉사", locationAndTime = "서울 강남구 · 매주 화 14:00"
                ),
                SavedVolunteer(
                    id = 2, "🌱", Color(0xFFE8F4E5),
                    "환경·생태", Color(0xFF539145), Color(0xFFE8F4E5), dDay = "D-8",
                    title = "한강 플로깅 환경 캠페인", locationAndTime = "서울 마포구 · 7월 12일"
                ),
                SavedVolunteer(
                    id = 3, "🎨", Color(0xFFF3E5F5),
                    "문화·예술", Color(0xFF7B1FA2), Color(0xFFF3E5F5),
                    title = "소외계층 아동 미술 클래스", locationAndTime = "서울 동작구 · 7월 19일"
                )
            )
        )
    }

    Scaffold(
        containerColor = VodaBgColor,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        top = 8.dp,
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = 0.dp
                    )
                )
                .fillMaxSize()
        ) {
            // 타이틀 및 배지 영역 고정 (패딩 유지)
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "찜한 봉사",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .background(VodaGreenMain, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("찜한 봉사 ${savedList.size}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // 스크롤 리스트 영역 (독립 스크롤 및 바텀바 밀착)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(savedList, key = { it.id }) { item ->
                    SavedVolunteerCard(
                        item = item,
                        onFavoriteClick = {
                            savedList = savedList.filterNot { it.id == item.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedVolunteerCard(
    item: SavedVolunteer,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(24.dp))
            // 💡 요구사항: #EDE8DC 색상으로 라운드 카드 테두리 선 주입
            .border(1.dp, VodaLineColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(item.emojiBg),
            contentAlignment = Alignment.Center
        ) {
            Text(item.emoji, fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(item.categoryBg, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(item.category, color = item.categoryColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                if (item.dDay != null) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFDF2E9), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(item.dDay, color = Color(0xFFE67E22), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = item.locationAndTime, fontSize = 11.sp, color = Color(0xFF888888))
        }

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "찜 취소",
            tint = VodaHeartColor,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // 💡 찜 해제 터치 시 불필요한 회색 사각형 인디케이션 필터링
                ) { onFavoriteClick() }
                .padding(2.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SavedScreenPreview() {
    MaterialTheme { SavedScreen() }
}