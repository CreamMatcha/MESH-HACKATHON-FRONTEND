package com.mesh.voda.presentation.activity

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── 모델 ────────────────────────────────────────────────────────────────────

enum class ActivityTab(val label: String, val count: Int) {
    APPLIED("신청한 봉사", 2),
    SCHEDULED("예정된 봉사", 3),
    COMPLETED("완료한 봉사", 7),
    HISTORY("활동 이력", 12)
}

data class ActivityItem(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val location: String,
    val statusLabel: String,
    val statusType: ActivityStatusType,
    val dDay: String? = null,
    val historyDate: String? = null
)

enum class ActivityStatusType { REVIEW, APPROVED, COMPLETED }

// ── 화면 ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    onNavigateToSettings: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val appliedItems = remember {
        listOf(
            ActivityItem("1", "한강 수질 환경 정화 캠페인", "환경", "7월 5일 (토) 09:00", "서울 마포구", "검토중", ActivityStatusType.REVIEW),
            ActivityItem("2", "유기견 임시보호 교육 프로그램", "동물", "7월 12일 (토) 10:00", "경기 성남시", "승인됨", ActivityStatusType.APPROVED),
        )
    }
    val scheduledItems = remember {
        listOf(
            ActivityItem("3", "유기견 임시보호 교육 프로그램", "동물", "7월 12일 (토) 10:00", "경기 성남시", "승인됨", ActivityStatusType.APPROVED, "D-3"),
            ActivityItem("4", "서울숲 생태 환경 정화", "환경", "7월 19일 (일) 09:00", "서울 성동구", "승인됨", ActivityStatusType.APPROVED, "D-10"),
            ActivityItem("5", "어린이 독서 지도 봉사", "교육", "7월 26일 (토) 13:00", "서울 노원구", "승인됨", ActivityStatusType.APPROVED, "D-17"),
        )
    }
    val completedItems = remember {
        listOf(
            ActivityItem("6", "마포구 홍제천 생태 환경 정화", "환경", "6월 21일 (일) 09:00", "서울 마포구", "완료", ActivityStatusType.COMPLETED),
            ActivityItem("7", "노인복지관 말벗 봉사", "복지", "6월 10일 (화) 14:00", "서울 강남구", "완료", ActivityStatusType.COMPLETED),
            ActivityItem("8", "망원 한강공원 쓰레기 수거", "환경", "5월 31일 (토) 10:00", "서울 마포구", "완료", ActivityStatusType.COMPLETED),
        )
    }
    val historyItems = remember {
        listOf(
            ActivityItem("9", "마포구 홍제천 생태 환경 정화", "환경", "2025년 6월 21일", "서울 마포구", "완료", ActivityStatusType.COMPLETED, historyDate = "2025년 6월 21일"),
            ActivityItem("10", "노인복지관 말벗 봉사", "복지", "2025년 6월 10일", "서울 강남구", "완료", ActivityStatusType.COMPLETED, historyDate = "2025년 6월 10일"),
            ActivityItem("11", "망원 한강공원 쓰레기 수거", "환경", "2025년 5월 31일", "서울 마포구", "완료", ActivityStatusType.COMPLETED, historyDate = "2025년 5월 31일"),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "내 활동",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "마이페이지",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 프로필 카드
            item { ProfileSummaryCard(onNavigateToSettings = onNavigateToSettings) }

            // 탭
            item {
                ActivityTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            // 탭별 컨텐츠
            when (selectedTab) {
                0 -> {
                    item { SectionLabel("신청 후 검토 중인 봉사", appliedItems.size) }
                    items(appliedItems, key = { it.id }) { item ->
                        AppliedActivityCard(item = item)
                    }
                    item { AppliedNoticeBox() }
                }
                1 -> {
                    item { SectionLabel("확정된 예정 봉사", scheduledItems.size) }
                    items(scheduledItems, key = { it.id }) { item ->
                        ScheduledActivityCard(item = item)
                    }
                }
                2 -> {
                    item { SectionLabel("이번 달", completedItems.size) }
                    items(completedItems, key = { it.id }) { item ->
                        CompletedActivityCard(item = item)
                    }
                }
                3 -> {
                    item { HistoryFilterRow() }
                    item {
                        Text(
                            "2025년",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                    items(historyItems, key = { it.id }) { item ->
                        HistoryCard(item = item)
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ── 프로필 요약 카드 ─────────────────────────────────────────────────────────

@Composable
private fun ProfileSummaryCard(onNavigateToSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        // 프로필 행
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, Modifier.size(22.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("김봉사", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.width(6.dp))
                    MbtiBadge()
                }
                Text(
                    "2024년 3월부터 활동 중",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Row(
                modifier = Modifier.clickable { onNavigateToSettings() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("마이페이지", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(Icons.Default.ChevronRight, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(12.dp))

        // 통계
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("12회" to "누적 봉사", "36h" to "활동 시간", "4곳" to "활동 기관").forEach { (value, label) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(value, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 프로그레스
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("다음 뱃지까지", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("12 / 20회", fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { 0.6f },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
            strokeCap = StrokeCap.Round
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "🥇 골드 봉사자 8회 남음",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun MbtiBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE8F5E9))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text("🌿 자연파 INFP", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
    }
}

// ── 탭 행 ─────────────────────────────────────────────────────────────────────

@Composable
private fun ActivityTabRow(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 16.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        divider = {
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(MaterialTheme.colorScheme.outlineVariant))
        },
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        ActivityTab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        tab.label,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (selectedTab == index) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 7.dp, vertical = 1.dp)
                    ) {
                        Text(
                            "${tab.count}",
                            fontSize = 10.sp,
                            color = if (selectedTab == index) MaterialTheme.colorScheme.surface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ── 공통 섹션 라벨 ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(label: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${count}건", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ── 신청한 봉사 카드 ──────────────────────────────────────────────────────────

@Composable
private fun AppliedActivityCard(item: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Column(modifier = Modifier.weight(1f)) {
            ActivityStatusPill(item.statusLabel, item.statusType)
            Spacer(Modifier.height(3.dp))
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            ActivityMetaRow(item.date, item.location)
        }
        Icon(Icons.Default.ChevronRight, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Divider()
}

@Composable
private fun AppliedNoticeBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(11.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Default.Info, null, Modifier.size(15.dp).padding(top = 1.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            "기관 검토 후 승인 여부를 알려드려요. 보통 1-3일 소요됩니다.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}

// ── 예정된 봉사 카드 ──────────────────────────────────────────────────────────

@Composable
private fun ScheduledActivityCard(item: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ActivityStatusPill(item.statusLabel, item.statusType)
                item.dDay?.let { DDayBadge(it) }
            }
            Spacer(Modifier.height(3.dp))
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            ActivityMetaRow(item.date, item.location)
        }
        Icon(Icons.Default.ChevronRight, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Divider()
}

// ── 완료한 봉사 카드 ──────────────────────────────────────────────────────────

@Composable
private fun CompletedActivityCard(item: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.surface)
        }
        Column(modifier = Modifier.weight(1f)) {
            CategoryBadge(item.category)
            Spacer(Modifier.height(3.dp))
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            ActivityMetaRow(item.date, item.location)
        }
        Icon(Icons.Default.ChevronRight, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Divider()
}

// ── 활동 이력 ─────────────────────────────────────────────────────────────────

@Composable
private fun HistoryFilterRow() {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(listOf("🌿 환경  7회", "💝 복지  3회", "📚 교육  2회")) { tag ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(tag, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun HistoryCard(item: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier.size(8.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    CategoryBadge(item.category)
                    Icon(Icons.Default.Check, null, Modifier.size(12.dp), tint = Color(0xFF388E3C))
                }
                Spacer(Modifier.height(3.dp))
                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.historyDate ?: "", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

// ── 공통 컴포넌트 ─────────────────────────────────────────────────────────────

@Composable
private fun ActivityStatusPill(label: String, type: ActivityStatusType) {
    val (bg, color) = when (type) {
        ActivityStatusType.REVIEW -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.tertiary
        ActivityStatusType.APPROVED -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
        ActivityStatusType.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(bg).padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(label, fontSize = 10.sp, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DDayBadge(dDay: String) {
    val isUrgent = dDay == "D-3"
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (isUrgent) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primaryContainer
            )
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            dDay,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUrgent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
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

@Composable
private fun ActivityMetaRow(date: String, location: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, null, Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(3.dp))
            Text(date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(3.dp))
            Text(location, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "내 활동 - 신청한 봉사")
@Composable
fun ActivityScreenPreview() {
    MaterialTheme {
        ActivityScreen()
    }
}