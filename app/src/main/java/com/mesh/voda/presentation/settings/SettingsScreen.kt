package com.mesh.voda.presentation.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R

// 피그마 디자인 시스템 반영 컬러
private val VodaBgColor = Color(0xFFFBF7EF)
private val VodaCardBgColor = Color(0xFFFFFFFF)
private val VodaGreenMain = Color(0xFF4E8A3F)
private val VodaGreenBadgeBg = Color(0xFFEBF5EB)
private val VodaTextGray = Color(0xFF888888)
private val VodaLineColor = Color(0xFFEDE8DC)

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToRetest: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    // 💡 [API 연결 주석 처리 부분]
    // val userProfile by viewModel.userProfile.collectAsState()
    // val userStats by viewModel.userStats.collectAsState()

    val userName = "강민지"
    val userType = "그린메이커형"
    val savedCount = "5"
    val completedCount = "2"
    val totalHours = "6h"
    val keywords = listOf("#체험형", "#체험형", "#체험형")

    Scaffold(
        containerColor = VodaBgColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VodaBgColor)
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "뒤로가기",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "마이페이지",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. 프로필 정보 카드
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VodaCardBgColor, RoundedCornerShape(24.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(VodaGreenBadgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌱", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌱", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userType,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = VodaGreenMain
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "프로필 수정",
                    tint = Color(0xFFCCCCCC),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. 대시보드 현황판
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VodaCardBgColor, RoundedCornerShape(24.dp))
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = savedCount, label = "찜")
                }
                Box(modifier = Modifier.size(1.dp, 28.dp).background(VodaLineColor))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = completedCount, label = "완료")
                }
                Box(modifier = Modifier.size(1.dp, 28.dp).background(VodaLineColor))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DashboardItem(value = totalHours, label = "누적 시간")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. 나의 봉사 키워드 영역
            Text(
                text = "나의 봉사 키워드",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                keywords.forEach { keyword ->
                    Box(
                        modifier = Modifier
                            .background(VodaGreenBadgeBg, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = keyword,
                            color = VodaGreenMain,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. 메뉴 리스트 영역
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MenuItem(
                    icon = painterResource(R.drawable.ic_settings_history),
                    title = "봉사 내역",
                    onClick = onNavigateToHistory
                )
                MenuItem(
                    icon = painterResource(R.drawable.ic_settings_retest),
                    title = "봉사 유형 다시 검사",
                    onClick = onNavigateToRetest
                )
                MenuItem(
                    icon = painterResource(R.drawable.ic_settings_notification),
                    title = "알림 설정",
                    onClick = onNavigateToNotifications
                )
                MenuItem(
                    icon = painterResource(R.drawable.ic_settings_support),
                    title = "고객센터",
                    onClick = onNavigateToSupport
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DashboardItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = VodaTextGray)
    }
}

@Composable
private fun MenuItem(
    icon: Painter,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,  // ✅ imageVector → painter
            contentDescription = null,
            tint = VodaGreenMain,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFDDDDDD),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}