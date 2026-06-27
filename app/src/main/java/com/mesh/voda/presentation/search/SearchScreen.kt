package com.mesh.voda.presentation.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mesh.voda.R
import com.mesh.voda.presentation.common.theme.VodaTheme

// --- 색상 ---

private val BgCream = Color(0xFFF5F2EA)
private val GreenPrimary = Color(0xFF4E8A3F)
private val GreenLight = Color(0xFFE8F2E3)

// --- 데이터 모델 ---

data class RecentSearchItem(val keyword: String)

data class SearchResultItem(
    val id: Int,
    val emoji: String,
    val emojiBg: Color,
    val status: String,
    val dDay: String?,
    val title: String,
    val detail: String,
    val isSaved: Boolean = false,
)

private data class CategoryItem(val emoji: String, val label: String, val bg: Color)

// --- 더미 데이터 ---

private val initialRecentKeywords = listOf(
    RecentSearchItem("한강 환경"),
    RecentSearchItem("유기견 봉사"),
    RecentSearchItem("어르신 발벗"),
)

private val categories = listOf(
    CategoryItem("🌿", "환경", Color(0xFFE8F2E3)),
    CategoryItem("👶", "아동", Color(0xFFFFDCED)),
    CategoryItem("🧓", "어르신", Color(0xFFF3DCE3)),
    CategoryItem("🐾", "동물", Color(0xFFE3F4D7)),
    CategoryItem("♿", "장애인", Color(0xFFE8E3F5)),
    CategoryItem("🏥", "의료", Color(0xFFF5C0C0)),
    CategoryItem("🌍", "다문화", Color(0xFFE3F0F5)),
    CategoryItem("🎨", "예술", Color(0xFFFFFEF6)),
    CategoryItem("🏘️", "지역사회", Color(0xFFF5F5E3)),
)

private val searchResults = listOf(
    SearchResultItem(1, "🌿", GreenLight, "모집중", "D-8", "한강 플로깅 환경 정화 캠페인", "서울 마포구 · 7월 12일 · 3시간"),
    SearchResultItem(2, "🌿", GreenLight, "모집중", null, "도시텃밭 가꾸기 & 수확물 기부", "서울 은평구 · 7월 11일 · 2.5시간", true),
    SearchResultItem(3, "🌿", GreenLight, "모집중", null, "도심 하천 생태 모니터링단", "서울 동대문구 · 7월 26일 · 3시간"),
)

// --- 필터 설정 ---

private data class FilterConfig(val key: String, val options: List<String>)

private val filterConfigs = listOf(
    FilterConfig("지역", listOf("전체", "서울", "경기", "인천", "부산", "대구")),
    FilterConfig("분야", listOf("전체", "환경", "아동", "어르신", "동물", "장애인", "의료", "다문화", "예술", "지역사회")),
    FilterConfig("모집중", listOf("전체", "모집중", "마감임박", "마감")),
)

private val filterDefaults = mapOf(
    "지역" to "전체",
    "분야" to "전체",
    "모집중" to "전체",
)

// --- 메인 화면 ---

@Composable
fun SearchScreen(onNavigateToMap: () -> Unit = {}, onNavigateToDetail: () -> Unit = {}) {
    var query by remember { mutableStateOf("") }
    var isResultState by remember { mutableStateOf(false) }
    var recentKeywords by remember { mutableStateOf(initialRecentKeywords) }
    var openFilter by remember { mutableStateOf<String?>(null) }
    var selectedFilters by remember { mutableStateOf(filterDefaults) }
    var pendingSelection by remember { mutableStateOf(filterDefaults) }

    Box(modifier = Modifier.fillMaxSize().background(BgCream)) {
        Column {
            SearchHeader(
                query = query,
                isResultState = isResultState,
                onQueryChange = { query = it },
                onSearch = { if (query.isNotBlank()) isResultState = true },
                onClear = { query = "" },
                onBack = { isResultState = false; query = "" },
                openFilter = openFilter,
                selectedFilters = selectedFilters,
                onFilterClick = { key ->
                    if (openFilter == key) {
                        openFilter = null
                    } else {
                        pendingSelection = selectedFilters.toMutableMap()
                        openFilter = key
                    }
                },
            )
            if (isResultState) {
                ResultsContent(results = searchResults, onNavigateToDetail = onNavigateToDetail)
            } else {
                EmptyContent(
                    recentKeywords = recentKeywords,
                    onDeleteRecent = { keyword -> recentKeywords = recentKeywords.filter { it.keyword != keyword } },
                    onClearAllRecent = { recentKeywords = emptyList() },
                    onKeywordClick = { keyword -> query = keyword; isResultState = true },
                    onCategoryClick = { category ->
                        query = category
                        selectedFilters = selectedFilters + ("분야" to category)
                        pendingSelection = pendingSelection + ("분야" to category)
                        isResultState = true
                    },
                    onNavigateToMap = onNavigateToMap,
                )
            }
        }

        // 드롭다운 외부 클릭 시 닫기
        if (openFilter != null) {
            Surface(
                onClick = { openFilter = null },
                color = Color.Transparent,
                modifier = Modifier.fillMaxSize().zIndex(0f)
            ) {}
        }

        // 필터 드롭다운 오버레이
        if (openFilter != null && isResultState) {
            val config = filterConfigs.first { it.key == openFilter }
            Box(
                modifier = Modifier
                    .padding(top = 130.dp, start = 16.dp)
                    .zIndex(1f)
            ) {
                FilterDropdown(
                    title = config.key,
                    options = config.options,
                    selected = pendingSelection[config.key] ?: config.options.first(),
                    onSelect = { pendingSelection = pendingSelection + (config.key to it) },
                    onReset = { pendingSelection = pendingSelection + (config.key to filterDefaults[config.key]!!) },
                    onApply = {
                        selectedFilters = pendingSelection.toMutableMap()
                        openFilter = null
                    },
                    onDismiss = { openFilter = null },
                )
            }
        }
    }
}

// --- 상단 검색 헤더 ---

@Composable
private fun SearchHeader(
    query: String,
    isResultState: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    openFilter: String? = null,
    selectedFilters: Map<String, String> = emptyMap(),
    onFilterClick: (String) -> Unit = {},
) {
    Column(modifier = Modifier.background(BgCream)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isResultState) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.search_ic_arrow_back),
                        contentDescription = "뒤로",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search_ic_search),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, color = Color.Black),
                        decorationBox = { inner ->
                            if (query.isEmpty()) Text("봉사명, 기관, 지역 검색", color = Color.Gray, fontSize = 15.sp)
                            inner()
                        }
                    )
                    if (query.isNotEmpty()) {
                        IconButton(onClick = onClear, modifier = Modifier.size(18.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.search_ic_close),
                                contentDescription = "지우기",
                                tint = Color.Gray,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.search_ic_mic),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
        if (isResultState) {
            FilterRow(
                openFilter = openFilter,
                selectedFilters = selectedFilters,
                onFilterClick = onFilterClick,
            )
        }
    }
}

// --- 필터 행 ---

@Composable
private fun FilterRow(
    openFilter: String?,
    selectedFilters: Map<String, String>,
    onFilterClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterConfigs.forEach { config ->
            val isOpen = openFilter == config.key
            val isActive = selectedFilters[config.key] != filterDefaults[config.key]
            Surface(
                onClick = { onFilterClick(config.key) },
                shape = RoundedCornerShape(16.dp),
                color = if (isActive) GreenPrimary else Color.White,
                border = BorderStroke(1.dp, if (isActive || isOpen) GreenPrimary else Color(0xFFDDDDDD))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isActive) selectedFilters[config.key]!! else config.key,
                        fontSize = 13.sp,
                        color = if (isActive) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.search_ic_chevron_down),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp).rotate(if (isOpen) 180f else 0f),
                        tint = if (isActive) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

// --- 필터 드롭다운 ---

@Composable
private fun FilterDropdown(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.width(200.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss, modifier = Modifier.size(20.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.search_ic_close),
                        contentDescription = "닫기",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0))
            options.forEach { option ->
                Surface(onClick = { onSelect(option) }, color = Color.Transparent) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                            color = if (option == selected) Color.Black else Color(0xFF666666)
                        )
                        if (option == selected) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = GreenPrimary)
                        }
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) { Text("초기화", fontSize = 13.sp, color = Color.Black) }
                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) { Text("적용", fontSize = 13.sp) }
            }
        }
    }
}

// --- 빈 상태 ---

@Composable
private fun EmptyContent(
    recentKeywords: List<RecentSearchItem>,
    onDeleteRecent: (String) -> Unit,
    onClearAllRecent: () -> Unit,
    onKeywordClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigateToMap: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        if (recentKeywords.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("최근 검색어", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onClearAllRecent, contentPadding = PaddingValues(0.dp)) {
                    Text("전체 지우기", fontSize = 13.sp, color = Color.Gray)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentKeywords.forEach { item ->
                    RecentChip(
                        keyword = item.keyword,
                        onDelete = { onDeleteRecent(item.keyword) },
                        onClick = { onKeywordClick(item.keyword) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Text("관심 분야로 찾기", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(12.dp))
        CategoryGrid(categories = categories, onCategoryClick = onCategoryClick)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("내 주변 봉사", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
            TextButton(onClick = onNavigateToMap, contentPadding = PaddingValues(0.dp)) {
                Text("지도 →", fontSize = 13.sp, color = GreenPrimary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        MapPreview(onClick = onNavigateToMap)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RecentChip(keyword: String, onDelete: () -> Unit, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 7.dp, bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(keyword, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onDelete, modifier = Modifier.size(16.dp)) {
                Icon(
                    painter = painterResource(R.drawable.search_ic_close),
                    contentDescription = "삭제",
                    modifier = Modifier.size(11.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid(categories: List<CategoryItem>, onCategoryClick: (String) -> Unit) {
    val rows = categories.chunked(4)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { item ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        CategoryCell(item = item, onClick = { onCategoryClick(item.label) })
                    }
                }
                repeat(4 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryCell(item: CategoryItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(onClick = onClick, shape = CircleShape, color = item.bg, modifier = Modifier.size(56.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text(item.emoji, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(item.label, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun MapPreview(onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFDDE8D8),
        modifier = Modifier.fillMaxWidth().height(144.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("🗺️", fontSize = 40.sp)
        }
    }
}

// --- 결과 상태 ---

@Composable
private fun ResultsContent(results: List<SearchResultItem>, onNavigateToDetail: () -> Unit = {}) {
    var savedState by remember { mutableStateOf(results.associate { it.id to it.isSaved }) }

    Column(modifier = Modifier.fillMaxSize().background(BgCream)) {
        Text(
            text = "총 ${results.size}건",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(results) { item ->
                ResultCard(
                    item = item,
                    isSaved = savedState[item.id] == true,
                    onToggleSave = { savedState = savedState + (item.id to !(savedState[item.id] ?: false)) },
                    onCardClick = onNavigateToDetail
                )
            }
        }
    }
}

@Composable
private fun ResultCard(
    item: SearchResultItem,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onCardClick: () -> Unit = {},
) {
    Surface(
        onClick = onCardClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(10.dp), color = item.emojiBg, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.emoji, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusBadge(item.status)
                    item.dDay?.let { DDayBadge(it) }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(item.detail, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onToggleSave, modifier = Modifier.size(36.dp)) {
                if (isSaved) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "저장",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.home_ic_heart),
                        contentDescription = "저장",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (bg, textColor) = when (status) {
        "모집중" -> Color(0xFFE8F5E9) to GreenPrimary
        "마감임박" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        else -> Color(0xFFF5F5F5) to Color.Gray
    }
    Surface(shape = RoundedCornerShape(4.dp), color = bg) {
        Text(status, fontSize = 11.sp, color = textColor, modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp))
    }
}

@Composable
private fun DDayBadge(dDay: String) {
    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFFF3E0)) {
        Text(dDay, fontSize = 11.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    VodaTheme {
        SearchScreen()
    }
}

