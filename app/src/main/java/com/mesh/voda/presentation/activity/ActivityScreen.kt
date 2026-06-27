package com.mesh.voda.presentation.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val VodaBgColor = Color(0xFFFBF9F1)
private val VodaCardBgColor = Color(0xFFFFFFFF)
private val VodaGreenDeepCard = Color(0xFF3B7356)
private val VodaGreenMain = Color(0xFF539145)
private val VodaGreenBadgeBg = Color(0xFFE8F4E5)
private val VodaButtonBg = Color(0xFFEBF5EB)

// 탭 상태 구분을 위한 Enum 정의
enum class ActivityTab(val title: String) {
    ALL("신청한"), UPCOMING("예정된"), COMPLETED("완료한")
}

data class ActivityVolunteer(
    val id: Int,
    val emoji: String,
    val emojiBg: Color,
    val status: String, // "예정" 또는 "✅ 완료"
    val statusColor: Color,
    val statusBg: Color,
    val title: String,
    val date: String,
    val duration: String,
    val needsAction: Boolean = false
)

@Composable
fun ActivityScreen() {
    // 1. 현재 선택된 탭 상태 관리 (기본값: 신청한)
    var selectedTab by remember { mutableStateOf(ActivityTab.ALL) }

    // 전체 데이터 원본 (나중에 백엔드에서 GET 요청으로 받아올 영역)
    val allActivities = listOf(
        ActivityVolunteer(
            id = 1, "🌱", Color(0xFFE8F4E5),
            status = "예정", statusColor = Color(0xFFD35400), statusBg = Color(0xFFFDF2E9),
            title = "한강 플로깅 환경 캠페인", date = "2026.07.12", duration = "3시간",
            needsAction = true
        ),
        ActivityVolunteer(
            id = 2, "👵", Color(0xFFFFE8E8),
            status = "✅ 완료", statusColor = VodaGreenMain, statusBg = VodaGreenBadgeBg,
            title = "독거어르신 밑반찬 나눔", date = "2026.06.21", duration = "3시간"
        )
    )

    // 2. 선택된 탭 상태에 따라 동적으로 리스트 필터링
    val filteredList = when (selectedTab) {
        ActivityTab.ALL -> allActivities
        ActivityTab.UPCOMING -> allActivities.filter { it.status == "예정" }
        ActivityTab.COMPLETED -> allActivities.filter { it.status.contains("완료") }
    }

    Scaffold(
        containerColor = VodaBgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "내 활동",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 상단 대시보드 현황판
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VodaGreenDeepCard, RoundedCornerShape(24.dp))
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 💡 [API 연결 주석 처리 부분]
                // 각 항목의 수치는 서버 응답 모델(allActivities.size 등)에 따라 실시간 매핑
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = "${allActivities.size}", label = "신청")
                }
                Box(modifier = Modifier.size(1.dp, 28.dp).background(Color(0x33FFFFFF)))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = "${allActivities.count { it.status.contains("완료") }}", label = "완료")
                }
                Box(modifier = Modifier.size(1.dp, 28.dp).background(Color(0x33FFFFFF)))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = "6", label = "누적 시간")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 소형 필터 배지 탭들 (클릭 가능하도록 바인딩)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActivityTab.values().forEach { tab ->
                    // 탭 기준에 맞는 데이터 개수 계산
                    val count = when(tab) {
                        ActivityTab.ALL -> allActivities.size
                        ActivityTab.UPCOMING -> allActivities.count { it.status == "예정" }
                        ActivityTab.COMPLETED -> allActivities.count { it.status.contains("완료") }
                    }

                    FilterBadge(
                        text = "${tab.title} $count",
                        isSelected = selectedTab == tab,
                        onClick = {
                            // 💡 [API 연결 주석 처리 부분]
                            // 탭마다 호출 주소가 다르다면 여기에서 viewModel.fetchData(tab) 형태로 서버 재요청 가능
                            selectedTab = tab
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 내 활동 리스트
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.id }) { item ->
                    ActivityCard(item)
                }
            }
        }
    }
}

@Composable
private fun DashboardItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xB3FFFFFF))
    }
}

@Composable
private fun FilterBadge(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) VodaGreenMain else VodaCardBgColor)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF666666),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ActivityCard(item: ActivityVolunteer) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(item.emojiBg),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .background(item.statusBg, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(item.status, color = item.statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📅 ${item.date}", fontSize = 11.sp, color = Color(0xFF888888))
                    Text(text = "⏱️ ${item.duration}", fontSize = 11.sp, color = Color(0xFF888888))
                }
            }
        }

        if (item.needsAction) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VodaButtonBg)
                    .clickable {
                        // 💡 [API 연결 주석 처리 부분]
                        // viewModelScope.launch { repository.completeActivity(item.id) }
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "활동 완료 처리하기", color = VodaGreenMain, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ActivityScreenPreview() {
    MaterialTheme { ActivityScreen() }
}