package com.mesh.voda.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R
import com.mesh.voda.presentation.common.theme.VodaTheme

// --- 색상 ---

private val BgCream = Color(0xFFF5F2EA)
private val GreenPrimary = Color(0xFF4E8A3F)
private val GreenDark = Color(0xFF3A6B2E)
private val GreenLight = Color(0xFFE8F2E3)

// --- 데이터 모델 ---

data class AiVolunteerItem(
    val id: Int,
    val emoji: String,
    val cardBg: Color,
    val title: String,
    val location: String,
    val date: String,
    val isSaved: Boolean = false,
)

data class LocationVolunteerItem(
    val id: Int,
    val distance: String,
    val title: String,
    val detail: String,
    val isSaved: Boolean = false,
)

// --- 더미 데이터 ---

private val aiItems = listOf(
    AiVolunteerItem(1, "🌿", Color(0xFFE8F2E3), "한강 플로깅 환경 캠페인", "서울 마포구", "7월 12일 (토)"),
    AiVolunteerItem(2, "🐾", Color(0xFFF5DDD8), "유기견 산책 & 미용 봉사", "경기 남양주", "7월 13일 (일)"),
    AiVolunteerItem(3, "🌍", Color(0xFFD8E8F5), "다문화 가정 언어 지원", "서울 성동구", "7월 19일 (토)"),
    AiVolunteerItem(4, "🎨", Color(0xFFF5EDD8), "어르신 미술 치료 봉사", "서울 노원구", "7월 26일 (토)"),
)

private val locationItems = listOf(
    LocationVolunteerItem(1, "0.6km", "홍제천 생태 환경 정화", "7월 5일 (토) 09:00 · 마포구"),
    LocationVolunteerItem(2, "1km", "~~~~~", "7월 7일 (월) 09:00 · 마포구"),
)

private val regionFilterChips = listOf("내 주변", "서울 마포구", "1km 이내", "모집중")

// --- 메인 화면 ---

@Composable
fun HomeScreen(
    onNavigateToRecommendation: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    var selectedRegionFilter by remember { mutableStateOf("내 주변") }

    Column(modifier = Modifier.fillMaxSize().background(BgCream)) {
        HomeTopBar(onNavigateToSettings = onNavigateToSettings)
        LazyColumn(contentPadding = PaddingValues(top = 12.dp)) {
            item { MbtiBannerCard() }
            item {
                SectionHeader(
                    title = "AI 맞춤 추천",
                    actionLabel = "전체 보기",
                    actionIconRes = R.drawable.home_ic_arrow_right,
                    modifier = Modifier.padding(top = 8.dp),
                    onAction = onNavigateToRecommendation
                )
            }
            item { AiRecommendSection(items = aiItems) }
            item {
                SectionHeader(
                    title = "지역 기반 봉사",
                    actionLabel = "지도 보기",
                    actionIconRes = R.drawable.home_ic_map,
                    modifier = Modifier.padding(top = 0.dp)
                )
            }
            item {
                RegionFilterChips(
                    chips = regionFilterChips,
                    selected = selectedRegionFilter,
                    onSelect = { selectedRegionFilter = it }
                )
            }
            items(locationItems) { item ->
                LocationVolunteerCard(item = item)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// --- 컴포넌트 ---

@Composable
private fun HomeTopBar(onNavigateToSettings: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 로고 아이콘
        Icon(
            painter = painterResource(R.drawable.home_ic_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "VoDa",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        // 프로필 버튼
        Surface(
            onClick = onNavigateToSettings,
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = Color(0xFFEEEBE3)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.home_ic_profile),
                    contentDescription = "프로필",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF888888)
                )
            }
        }
    }
}

@Composable
private fun MbtiBannerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = GreenDark
    ) {
        Box(contentAlignment = Alignment.Center) {
            // 장식용 원 (Figma: #7FC46E, opacity 20%)
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-32).dp)
                    .background(Color(0x337FC46E), CircleShape)
            )
            // 장식용 원 2 (Figma: #A8D898, opacity 10%)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = 8.dp)
                    .background(Color(0x1AA8D898), CircleShape)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color(0x2EFFFFFF)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🌱", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "나의 봉사 MBTI",
                        fontSize = 11.sp,
                        color = Color(0xCCC8E8B8)
                    )
                    Text(
                        text = "그린메이커형",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "환경·생태 분야와 가장 잘 맞아요",
                        fontSize = 12.sp,
                        color = Color(0xBFC8E8B8)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    actionIconRes: Int,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 8.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onAction, contentPadding = PaddingValues(0.dp)) {
            Text(text = actionLabel, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(actionIconRes),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun AiRecommendSection(items: List<AiVolunteerItem>) {
    var savedIds by remember { mutableStateOf(setOf<Int>()) }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            AiVolunteerCard(
                item = item.copy(isSaved = item.id in savedIds),
                onSaveToggle = {
                    savedIds = if (item.id in savedIds) savedIds - item.id else savedIds + item.id
                }
            )
        }
    }
}

@Composable
private fun AiVolunteerCard(item: AiVolunteerItem, onSaveToggle: () -> Unit) {
    Surface(
        modifier = Modifier.width(168.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEDE8DC))
    ) {
        Column {
            // 컬러 상단 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(item.cardBg, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 36.sp)
                // +AI 추천 뱃지
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(20.dp),
                    color = GreenPrimary
                ) {
                    Text(
                        text = "+ AI 추천",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                // 찜 버튼
                IconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (item.isSaved) R.drawable.home_ic_heart_filled else R.drawable.home_ic_heart
                        ),
                        contentDescription = "찜",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            // 텍스트 하단 영역
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.home_ic_location_small),
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFFAAAAAA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.location, fontSize = 10.sp, color = Color(0xFFAAAAAA))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.home_ic_calendar),
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFFAAAAAA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.date, fontSize = 10.sp, color = Color(0xFFAAAAAA))
                }
            }
        }
    }
}

@Composable
private fun RegionFilterChips(chips: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            val isSelected = chip == selected
            Surface(
                onClick = { onSelect(chip) },
                shape = RoundedCornerShape(50),
                color = if (isSelected) GreenPrimary else Color.White,
                border = BorderStroke(1.dp, if (isSelected) GreenPrimary else Color(0xFFE0D8CA))
            ) {
                Text(
                    text = chip,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color.White else Color(0xFF666666),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun LocationVolunteerCard(item: LocationVolunteerItem) {
    var isSaved by remember { mutableStateOf(item.isSaved) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEDE8DC))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이모지 썸네일
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(10.dp),
                color = GreenLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🌱", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                // 거리
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.home_ic_location),
                        contentDescription = null,
                        modifier = Modifier.size(11.dp),
                        tint = GreenPrimary
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = item.distance, fontSize = 11.sp, color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = item.detail, fontSize = 11.sp, color = Color(0xFFAAAAAA), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { isSaved = !isSaved }, modifier = Modifier.size(36.dp)) {
                Icon(
                    painter = painterResource(
                        if (isSaved) R.drawable.home_ic_heart_filled else R.drawable.home_ic_heart
                    ),
                    contentDescription = "찜",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    VodaTheme {
        HomeScreen()
    }
}

