package com.mesh.voda.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 피그마 디자인 기반 전용 컬러 구성
private val VodaBgColor = Color(0xFFFBF9F1) // 전체 화면 배경 (연한 아이보리)
private val VodaCardBgColor = Color(0xFFFFFFFF) // 화이트 카드 배경
private val VodaGreenMain = Color(0xFF539145) // 메인 그린 텍스트/아이콘 컬러
private val VodaGreenBadgeBg = Color(0xFFE8F4E5) // 그린 배지 배경 컬러
private val VodaGrayText = Color(0xFF767676) // 서브 텍스트 회색
private val VodaDividerColor = Color(0xFFEFEFEF) // 메뉴 구분선 컬러

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToFavoriteVolunteers: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToRecheckType: () -> Unit = {},
    onNavigateToNotificationSettings: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
) {
    Scaffold(
        containerColor = VodaBgColor,
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "마이페이지",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VodaBgColor
                )
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. 프로필 영역 (상단 이미지, 이름, 유형 배지)
            ProfileSection(
                name = "강민지",
                badgeText = "그린메이커형",
                onClick = onNavigateToEditProfile
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. 활동 통계 카드 (찜, 완료, 누적 시간)
            StatCard(
                favoriteCount = "5",
                completeCount = "2",
                totalHours = "6h"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. 나의 봉사 키워드 섹션
            KeywordSection(keyword = "체험형")

            Spacer(modifier = Modifier.height(20.dp))

            // 4. 메뉴 리스트 그룹 카드
            MenuCardGroup(
                onFavoriteClick = onNavigateToFavoriteVolunteers,
                onHistoryClick = onNavigateToHistory,
                onRecheckClick = onNavigateToRecheckType,
                onNotificationClick = onNavigateToNotificationSettings,
                onSupportClick = onNavigateToSupport
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ─── 1. 프로필 섹션 ─────────────────────────────────────────────────────────────

@Composable
private fun ProfileSection(
    name: String,
    badgeText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 새싹 프로필 박스 이미지 형태화
        Box(
            modifier = Modifier.size(86.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VodaGreenBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                // 피그마 내 새싹 이모지 혹은 아이콘 대체 표현
                Text("🌱", fontSize = 40.sp)
            }

            // 이미지 우하단 돋보기/설정 아이콘 뱃지
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(VodaGreenMain)
                    .border(2.dp, VodaBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))

            // 그린메이커형 유형 배지
            Row(
                modifier = Modifier
                    .background(VodaGreenBadgeBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("🌱", fontSize = 11.sp)
                Text(
                    text = badgeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VodaGreenMain
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "프로필 상세",
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(24.dp)
        )
    }
}

// ─── 2. 통계 카드 ───────────────────────────────────────────────────────────────

@Composable
private fun StatCard(
    favoriteCount: String,
    completeCount: String,
    totalHours: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(24.dp))
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 찜 영역 (전체 너비의 1/3 차지)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            StatItem(value = favoriteCount, label = "찜")
        }

        StatVerticalDivider()

        // 2. 완료 영역 (전체 너비의 1/3 차지)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            StatItem(value = completeCount, label = "완료")
        }

        StatVerticalDivider()

        // 3. 누적 시간 영역 (전체 너비의 1/3 차지)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            StatItem(value = totalHours, label = "누적 시간")
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = VodaGrayText
        )
    }
}

@Composable
private fun StatVerticalDivider() {
    Box(
        modifier = Modifier
            .height(28.dp)
            .width(1.dp)
            .background(Color(0xFFEAEAEA))
    )
}

// ─── 3. 나의 봉사 키워드 섹션 ────────────────────────────────────────────────────

@Composable
private fun KeywordSection(keyword: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "나의 봉사 키워드",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row {
            Box(
                modifier = Modifier
                    .background(VodaGreenBadgeBg, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$keyword",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = VodaGreenMain
                )
            }
        }
    }
}

// ─── 4. 메뉴 카드 그룹 리스트 ────────────────────────────────────────────────────

@Composable
private fun MenuCardGroup(
    onFavoriteClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onRecheckClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VodaCardBgColor, RoundedCornerShape(24.dp))
    ) {
        MenuRowItem(
            icon = Icons.Default.FavoriteBorder,
            title = "찜한 봉사",
            onClick = onFavoriteClick
        )
        MenuHorizontalDivider()
        MenuRowItem(
            icon = Icons.Default.History,
            title = "봉사 내역",
            onClick = onHistoryClick
        )
        MenuHorizontalDivider()
        MenuRowItem(
            icon = Icons.Default.Refresh,
            title = "봉사 유형 다시 검사",
            onClick = onRecheckClick
        )
        MenuHorizontalDivider()
        MenuRowItem(
            icon = Icons.Default.NotificationsNone,
            title = "알림 설정",
            onClick = onNotificationClick
        )
        MenuHorizontalDivider()
        MenuRowItem(
            icon = Icons.Default.HelpOutline,
            title = "고객센터",
            onClick = onSupportClick,
            isLast = true
        )
    }
}

@Composable
private fun MenuRowItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    val bottomRadius = if (isLast) 24.dp else 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = bottomRadius, bottomEnd = bottomRadius))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VodaGreenMain,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFD1D1D1),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
private fun MenuHorizontalDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = VodaDividerColor,
        thickness = 1.dp
    )
}

// ─── 미리보기 스크린 ─────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
fun SettingsScreenFigmaPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}