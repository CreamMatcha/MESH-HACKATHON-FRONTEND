package com.mesh.voda.presentation.saved

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── 모델 ────────────────────────────────────────────────────────────────────

enum class SavedTab { SAVED, RECENT }

enum class VolunteerStatus(val label: String) {
    RECRUITING("모집중"),
    CLOSING("마감임박"),
    CLOSED("마감")
}

data class VolunteerItem(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val schedule: String,
    val status: VolunteerStatus,
    val isBookmarked: Boolean = false,
    val viewedAt: String? = null  // "오늘", "어제" 등 (최근 본 봉사용)
)

// ── 화면 ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onNavigateToVolunteerDetail: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(SavedTab.SAVED) }
    var showEmpty by remember { mutableStateOf(false) }

    val savedItems = remember {
        listOf(
            VolunteerItem("1", "한강 수질 환경 정화 캠페인", "환경", "서울 마포구", "7월 5일 (토) 09:00", VolunteerStatus.RECRUITING, true),
            VolunteerItem("2", "유기견 임시보호 교육 프로그램", "동물", "경기 성남시", "7월 12일 (토) 10:00", VolunteerStatus.CLOSING, true),
            VolunteerItem("3", "노인복지관 말벗 봉사", "복지", "서울 강남구", "매주 화요일 14:00", VolunteerStatus.RECRUITING, true),
            VolunteerItem("4", "도심 텃밭 가꾸기 봉사단", "환경", "서울 강동구", "7월 26일 (토)", VolunteerStatus.CLOSED, true),
            VolunteerItem("5", "어린이 독서 지도 봉사", "교육", "서울 노원구", "7월 19일 (토) 13:00", VolunteerStatus.RECRUITING, true),
        )
    }

    val recentGroups = remember {
        mapOf(
            "오늘" to listOf(
                VolunteerItem("6", "서울숲 생태 환경 정화 캠페인", "환경", "서울 성동구", "7월 6일 (일) 10:00", VolunteerStatus.RECRUITING, false, "오늘"),
                VolunteerItem("7", "강남 어르신 말벗 봉사", "복지", "서울 강남구", "매주 화요일", VolunteerStatus.RECRUITING, true, "오늘"),
                VolunteerItem("8", "한강 수질 모니터링 조사단", "환경", "서울 마포구", "7월 12일 (토)", VolunteerStatus.CLOSING, false, "오늘"),
            ),
            "어제" to listOf(
                VolunteerItem("9", "장애인 생활 이동 지원 봉사", "복지", "서울 은평구", "7월 8일 (화)", VolunteerStatus.RECRUITING, false, "어제"),
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "찜",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                            .clickable { showEmpty = !showEmpty }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (showEmpty) "데이터 표시" else "빈 상태 보기",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 세그먼트 탭
            SavedSegmentTab(
                selectedTab = selectedTab,
                savedCount = if (showEmpty) 0 else savedItems.size,
                recentCount = recentGroups.values.sumOf { it.size },
                onTabSelected = { selectedTab = it }
            )

            when (selectedTab) {
                SavedTab.SAVED -> {
                    if (showEmpty || savedItems.isEmpty()) {
                        SavedEmptyState()
                    } else {
                        SavedVolunteerList(
                            items = savedItems,
                            totalCount = savedItems.size,
                            onItemClick = onNavigateToVolunteerDetail
                        )
                    }
                }
                SavedTab.RECENT -> {
                    RecentVolunteerList(
                        groups = recentGroups,
                        totalCount = recentGroups.values.sumOf { it.size },
                        onItemClick = onNavigateToVolunteerDetail
                    )
                }
            }
        }
    }
}

// ── 세그먼트 탭 ──────────────────────────────────────────────────────────────

@Composable
private fun SavedSegmentTab(
    selectedTab: SavedTab,
    savedCount: Int,
    recentCount: Int,
    onTabSelected: (SavedTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            SavedTab.SAVED to "저장한 봉사 $savedCount",
            SavedTab.RECENT to "최근 본 봉사 $recentCount"
        ).forEach { (tab, label) ->
            val isSelected = selectedTab == tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ── 저장한 봉사 리스트 ───────────────────────────────────────────────────────

@Composable
private fun SavedVolunteerList(
    items: List<VolunteerItem>,
    totalCount: Int,
    onItemClick: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "총 ${totalCount}개",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = "최신순",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items, key = { it.id }) { item ->
                VolunteerCard(
                    item = item,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

// ── 최근 본 봉사 리스트 ──────────────────────────────────────────────────────

@Composable
private fun RecentVolunteerList(
    groups: Map<String, List<VolunteerItem>>,
    totalCount: Int,
    onItemClick: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "총 ${totalCount}개",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text("최신순", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            groups.forEach { (dateLabel, items) ->
                item {
                    Text(
                        text = dateLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(items, key = { it.id }) { item ->
                    VolunteerCard(
                        item = item,
                        onClick = { onItemClick(item.id) }
                    )
                }
            }
        }
    }
}

// ── 봉사 카드 ────────────────────────────────────────────────────────────────

@Composable
private fun VolunteerCard(
    item: VolunteerItem,
    onClick: () -> Unit
) {
    var bookmarked by remember { mutableStateOf(item.isBookmarked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 썸네일
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        // 정보
        Column(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StatusBadge(item.status)
                CategoryBadge(item.category)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(2.dp))
                Text(item.location, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(2.dp))
                Text(item.schedule, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // 찜 버튼
        IconButton(onClick = { bookmarked = !bookmarked }) {
            Icon(
                imageVector = if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (bookmarked) "찜 취소" else "찜하기",
                tint = if (bookmarked) Color(0xFFE00050) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(0.5.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

// ── 빈 상태 ──────────────────────────────────────────────────────────────────

@Composable
private fun SavedEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "아직 찜한 봉사가 없어요",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "관심 있는 봉사를 저장하고\n나중에 편하게 신청해보세요",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onSurface)
                .clickable { }
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                "봉사 둘러보기 →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}

// ── 뱃지 ─────────────────────────────────────────────────────────────────────

@Composable
private fun StatusBadge(status: VolunteerStatus) {
    val (bg, textColor) = when (status) {
        VolunteerStatus.RECRUITING -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
        VolunteerStatus.CLOSING -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.tertiary
        VolunteerStatus.CLOSED -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(status.label, fontSize = 10.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CategoryBadge(category: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(category, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "찜 - 저장한 봉사")
@Composable
fun SavedScreenPreview() {
    MaterialTheme {
        SavedScreen()
    }
}