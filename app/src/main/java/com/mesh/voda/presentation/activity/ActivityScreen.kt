package com.mesh.voda.presentation.activity

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R

private val VodaBgColor = Color(0xFFFBF7EF)
private val VodaCardBgColor = Color(0xFFFFFFFF)
private val VodaGreenDeepCard = Color(0xFF2C5E3B)
private val VodaGreenMain = Color(0xFF4E8A3F)
private val VodaGreenBadgeBg = Color(0xFFEBF5EB)
private val VodaTextGray = Color(0xFF888888)
private val VodaLineColor = Color(0xFFEDE8DC)

enum class ActivityTab(val title: String) {
    ALL("신청한 봉사"), UPCOMING("완료한 봉사"), COMPLETED("활동 이력")
}

// 데이터 모델 동일
data class ActivityVolunteer(
    val id: Int, val emoji: String, val emojiBg: Color, val status: String,
    val statusColor: Color, val statusBg: Color, val title: String,
    val date: String, val duration: String, val needsAction: Boolean = false, val category: String = ""
)

@Composable
fun ActivityScreen(
    onNavigateToSettings: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(ActivityTab.ALL) }

    val allActivities = listOf(
        ActivityVolunteer(
            id = 1, "🌱", Color(0xFFEBF5EB), "예정", Color(0xFFE67E22), Color(0xFFFDF2E9),
            "한강 플로깅 환경 캠페인", "2026.07.12", "3시간", needsAction = true, category = "환경"
        ),
        ActivityVolunteer(
            id = 2, "👶", Color(0xFFFFF5E6), "✅ 완료", VodaGreenMain, VodaGreenBadgeBg,
            "독거어르신 밑반찬 나눔", "2026.06.21", "3시간", category = "복지"
        ),
        ActivityVolunteer(
            id = 3, "🌱", Color(0xFFEBF5EB), "✅ 완료", VodaGreenMain, VodaGreenBadgeBg,
            "마포구 홍제천 생태 환경 정화", "2025.06.21", "4시간", category = "환경"
        ),
        ActivityVolunteer(
            id = 4, "🌱", Color(0xFFEBF5EB), "✅ 완료", VodaGreenMain, VodaGreenBadgeBg,
            "한강 플로깅 환경 캠페인", "2025.05.03", "2시간", category = "환경"
        )
    )

    val filteredList = when (selectedTab) {
        ActivityTab.ALL -> allActivities
        ActivityTab.UPCOMING -> allActivities.filter { it.status == "예정" }
        ActivityTab.COMPLETED -> allActivities.filter { it.status.contains("완료") && it.date.startsWith("2025") }
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
                        top = 8.dp, // 💡 기존 calculateTopPadding() 대신 고정 패딩 주입으로 상단 여백 제거
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = 0.dp // 하단 바텀바와 밀착
                    )
                )
                .fillMaxSize()
        ) {
            // 📌 [고정 영역 시작]
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("내 활동", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab == ActivityTab.COMPLETED) {
                    ProfileDetailCard(onNavigateToSettings = onNavigateToSettings)
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VodaGreenDeepCard, RoundedCornerShape(24.dp))
                            .padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { DashboardItem("4", "신청") }
                        Box(modifier = Modifier.size(1.dp, 28.dp).background(Color(0x26FFFFFF)))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { DashboardItem("2", "완료") }
                        Box(modifier = Modifier.size(1.dp, 28.dp).background(Color(0x26FFFFFF)))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { DashboardItem("6", "누적 시간") }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 탭 바
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(VodaLineColor).align(Alignment.BottomCenter))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ActivityTab.values().forEach { tab ->
                            val count = when(tab) {
                                ActivityTab.ALL -> 4
                                ActivityTab.UPCOMING -> 2
                                ActivityTab.COMPLETED -> 12
                            }
                            val isSelected = selectedTab == tab

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { selectedTab = tab }
                                    .padding(top = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = tab.title, fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) VodaGreenMain else VodaTextGray
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(if (isSelected) VodaGreenMain else Color(0xFFE0E0E0), CircleShape)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("$count", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (isSelected) {
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(VodaGreenMain))
                                } else {
                                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.Transparent))
                                }
                            }
                        }
                    }
                }
            }
            // 📌 [고정 영역 끝]

            // 🔄 [스크롤 영역 시작]
            if (selectedTab == ActivityTab.COMPLETED) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
                ) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CategoryChip("🌱 환경 7회", VodaGreenMain, Color.White)
                            CategoryChip("💓 복지 3회", Color(0xFFFFEBF0), Color(0xFFE55381))
                            CategoryChip("📚 교육 2회", Color(0xFFF3E5F5), Color(0xFF7B1FA2))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("2025년", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("10회", fontSize = 13.sp, color = VodaTextGray)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(filteredList, key = { it.id }) { item ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Canvas(modifier = Modifier.matchParentSize().padding(start = 11.dp)) {
                                drawLine(color = VodaLineColor, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 2.dp.toPx())
                            }
                            TimelineCard(item)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 0.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        ActivityCard(item)
                    }
                }
            }
            // 🔄 [스크롤 영역 끝]
        }
    }
}

@Composable
private fun ProfileDetailCard(onNavigateToSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(24.dp))
            .border(1.dp, VodaLineColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(54.dp).background(Color(0xFFEFECE5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_activity_profile),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("강민지", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.background(VodaGreenBadgeBg, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        Text("그린메이커형", color = VodaGreenMain, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("2024년 3월부터 활동 중", fontSize = 13.sp, color = VodaTextGray)
            }
            Row(
                modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onNavigateToSettings() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("마이페이지", fontSize = 13.sp, color = Color(0xFF999999))
                Spacer(modifier = Modifier.width(2.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFBBBBBB), modifier = Modifier.size(14.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth().background(VodaBgColor, RoundedCornerShape(16.dp)).padding(vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("12회", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111111))
                Spacer(modifier = Modifier.height(4.dp))
                Text("누적 봉사", fontSize = 12.sp, color = VodaTextGray)
            }
            Box(modifier = Modifier.size(1.dp, 32.dp).background(VodaLineColor))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("36h", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111111))
                Spacer(modifier = Modifier.height(4.dp))
                Text("활동 시간", fontSize = 12.sp, color = VodaTextGray)
            }
            Box(modifier = Modifier.size(1.dp, 32.dp).background(VodaLineColor))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("4곳", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111111))
                Spacer(modifier = Modifier.height(4.dp))
                Text("활동 기관", fontSize = 12.sp, color = VodaTextGray)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("다음 뱃지까지", fontSize = 13.sp, color = Color(0xFF555555))
            Text("12 / 20회", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VodaGreenMain)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(progress = { 12f / 20f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = VodaGreenMain, trackColor = Color(0xFFEAE6DC))
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⭐", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("골드 봉사자까지 8회 남음", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF8A8273))
        }
    }
}

@Composable
private fun ActivityCard(item: ActivityVolunteer) {
    Column(modifier = Modifier.fillMaxWidth().background(VodaCardBgColor, RoundedCornerShape(24.dp)).border(1.dp, VodaLineColor, RoundedCornerShape(24.dp)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(54.dp).clip(RoundedCornerShape(14.dp)).background(item.emojiBg), contentAlignment = Alignment.Center) { Text(item.emoji, fontSize = 24.sp) }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.background(item.statusBg, RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) { Text(item.status, color = item.statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📅 ${item.date}", fontSize = 11.sp, color = VodaTextGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "⏱️ ${item.duration}", fontSize = 11.sp, color = VodaTextGray)
                }
            }
        }
        if (item.needsAction) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFEBF5EB)).clickable { }.padding(vertical = 10.dp), contentAlignment = Alignment.Center) { Text(text = "활동 완료 처리하기", color = VodaGreenMain, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun TimelineCard(item: ActivityVolunteer) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.padding(top = 22.dp).size(14.dp).background(VodaBgColor, CircleShape).background(Color(0xFFCDC8BC), CircleShape), contentAlignment = Alignment.Center) { Box(modifier = Modifier.size(8.dp).background(VodaBgColor, CircleShape)) }
        Spacer(modifier = Modifier.width(16.dp))
        Row(modifier = Modifier.weight(1f).background(VodaCardBgColor, RoundedCornerShape(24.dp)).border(1.dp, VodaLineColor, RoundedCornerShape(24.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(item.emojiBg), contentAlignment = Alignment.Center) { Text(item.emoji, fontSize = 22.sp) }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.background(VodaGreenBadgeBg, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) { Text(item.category, color = VodaGreenMain, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(painter = painterResource(id = R.drawable.ic_activity_check), contentDescription = "체크 표시", modifier = Modifier.size(14.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.date, fontSize = 11.sp, color = VodaTextGray)
            }
        }
    }
}

@Composable
private fun CategoryChip(text: String, bgColor: Color, textColor: Color) {
    Box(modifier = Modifier.background(bgColor, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) { Text(text = text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun DashboardItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xB3FFFFFF))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun ActivityScreenPreview() {
    MaterialTheme { ActivityScreen() }
}