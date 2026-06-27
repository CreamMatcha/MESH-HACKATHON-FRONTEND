package com.mesh.voda.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 데이터 모델 ---

data class AiVolunteerItem(
    val id: Int,
    val title: String,
    val location: String,
    val date: String,
    val isSaved: Boolean = false,
)

data class LocationVolunteerItem(
    val id: Int,
    val distance: String,
    val title: String,
    val dateTime: String,
    val address: String,
    val isSaved: Boolean = false,
)

data class PopularVolunteerItem(
    val rank: Int,
    val title: String,
    val location: String,
    val date: String,
    val participantCount: String,
)

// --- 임시 더미 데이터 ---

private val aiItems = listOf(
    AiVolunteerItem(1, "한강 수질 환경 정화 봉사", "서울 마포구", "7월 5일 (토)"),
    AiVolunteerItem(2, "유기견 임시보호 교육 프로그램", "경기 성남시", "7월 12일 (토)"),
    AiVolunteerItem(3, "도심 텃밭 가꾸기 봉사단", "서울 강동구", "7월 19일 (토)"),
    AiVolunteerItem(4, "어린이 생태 탐방 도우미", "서울 노원구", "7월 26일 (토)"),
)

private val locationItems = listOf(
    LocationVolunteerItem(1, "0.6km", "마포구 홍제천 생태 환경 정화", "7월 5일 (토) 09:00", "서울 마포구 홍제천"),
    LocationVolunteerItem(2, "1.2km", "망원 한강공원 쓰레기 수거 봉사", "7월 7일 (월) 10:00", "서울 마포구 망원한강공원"),
    LocationVolunteerItem(3, "2.4km", "은평 장애인복지관 생활 지원", "7월 9일 (수) 14:00", "서울 은평구 불광동"),
)

private val popularItems = listOf(
    PopularVolunteerItem(1, "서울숲 생태 환경 정화 캠페인", "성동구", "7월 5일", "+35명"),
    PopularVolunteerItem(2, "강남 어르신 말벗 봉사", "강남구", "매주 화요일", "+46명"),
    PopularVolunteerItem(3, "한강 수질 모니터링 조사단", "마포구", "7월 12일", "+57명"),
)

private val regionFilterChips = listOf("내 주변", "서울 마포구", "1km 이내", "모집중")

// --- 메인 화면 ---

@Composable
fun HomeScreen() {
    var selectedRegionFilter by remember { mutableStateOf("내 주변") }
    var selectedPopularTab by remember { mutableStateOf("이번 주") }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HomeTopBar()
        LazyColumn {
            item { MbtiBannerCard() }
            item {
                SectionHeader(
                    title = "AI 맞춤 추천",
                    actionLabel = "전체 보기",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item { AiRecommendSection(items = aiItems) }
            item {
                SectionHeader(
                    title = "지역 기반 봉사",
                    actionLabel = "지도 보기",
                    modifier = Modifier.padding(top = 8.dp)
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
            item {
                SectionHeader(
                    title = "인기 봉사 🔥",
                    actionLabel = "전체 보기",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                PopularTabRow(
                    selected = selectedPopularTab,
                    onSelect = { selectedPopularTab = it }
                )
            }
            items(popularItems) { item ->
                PopularVolunteerCard(item = item)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// --- 컴포넌트 ---

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 로고
        Surface(
            modifier = Modifier.size(30.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {}
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "VoDa",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        // 프로필 버튼
        IconButton(onClick = {}) {
            Icon(Icons.Default.Person, contentDescription = "프로필")
        }
    }
}

@Composable
private fun MbtiBannerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아바타 아이콘
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color(0xFFE0E0E0)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "나의 봉사 MBTI",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "자연 친화 유형",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "환경·생태 분야와 가장 잘 맞아요",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionLabel: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
            Text(text = "$actionLabel >", fontSize = 13.sp, color = Color.Gray)
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
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column {
            // 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFEEEEEE))
            ) {
                // AI 추천 뱃지
                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD))
                ) {
                    Text(
                        text = "✦ AI 추천",
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                // 찜 버튼
                IconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (item.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "찜",
                        tint = if (item.isSaved) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            // 텍스트 영역
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.location, fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.date, fontSize = 12.sp, color = Color.Gray)
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
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            val isSelected = chip == selected
            Surface(
                onClick = { onSelect(chip) },
                shape = RoundedCornerShape(50),
                color = if (isSelected) Color.Black else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color.Black else Color(0xFFDDDDDD))
            ) {
                Text(
                    text = chip,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun LocationVolunteerCard(item: LocationVolunteerItem) {
    var isSaved by remember { mutableStateOf(item.isSaved) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 썸네일
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEEEEE))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            // 거리 뱃지
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF0F0F0)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = item.distance, fontSize = 11.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.dateTime, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.address, fontSize = 12.sp, color = Color.Gray)
            }
        }
        // 찜 버튼
        IconButton(onClick = { isSaved = !isSaved }) {
            Icon(
                imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "찜",
                tint = if (isSaved) Color.Red else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color(0xFFF0F0F0))
}

@Composable
private fun PopularTabRow(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("이번 주", "이번 달").forEach { tab ->
            val isSelected = tab == selected
            Surface(
                onClick = { onSelect(tab) },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) Color.Black else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color.Black else Color(0xFFDDDDDD))
            ) {
                Text(
                    text = tab,
                    fontSize = 14.sp,
                    color = if (isSelected) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun PopularVolunteerCard(item: PopularVolunteerItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 순위
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.rank.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        // 썸네일
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEEEEE))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (item.rank == 1) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Text(
                            text = "🔥 인기",
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = item.location, fontSize = 12.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = item.date, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        // 참여자 수
        Text(
            text = item.participantCount,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = Color(0xFFF0F0F0))
}
