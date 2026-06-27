package com.mesh.voda.presentation.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R
import com.mesh.voda.presentation.common.theme.VodaTheme

private val BgCream = Color(0xFFF5F2EA)
private val GreenPrimary = Color(0xFF4E8A3F)
private val CardBorder = Color(0xFFEDE8DC)

private data class MapPin(
    val label: String,
    val color: Color,
    val x: Dp,
    val y: Dp,
)

private val mapPins = listOf(
    MapPin("환경", GreenPrimary,         x = 118.dp, y = 76.dp),
    MapPin("복지", Color(0xFFC05878),    x = 55.dp,  y = 218.dp),
    MapPin("교육", Color(0xFF7B52A8),    x = 231.dp, y = 166.dp),
    MapPin("동물", Color(0xFFC07840),    x = 273.dp, y = 81.dp),
    MapPin("환경", GreenPrimary,         x = 168.dp, y = 306.dp),
    MapPin("환경", GreenPrimary,         x = 23.dp,  y = 296.dp),
    MapPin("교육", Color(0xFF7B52A8),    x = 313.dp, y = 256.dp),
)

private val filterChips = listOf("환경", "복지", "교육", "동물", "모집중")

private val categoryColors = mapOf(
    "환경"  to GreenPrimary,
    "복지"  to Color(0xFFC05878),
    "교육"  to Color(0xFF7B52A8),
    "동물"  to Color(0xFFC07840),
    "모집중" to GreenPrimary,
)

@Composable
fun MapScreen(onBack: () -> Unit = {}, onNavigateToDetail: () -> Unit = {}) {
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(BgCream)) {
        Column(modifier = Modifier.fillMaxSize()) {
            MapTopBar(onBack = onBack)
            MapFilterRow(
                selectedFilter = selectedFilter,
                onFilterClick = { label ->
                    selectedFilter = if (selectedFilter == label) null else label
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MapArea(selectedFilter = selectedFilter)
            }
            MapBottomSheet(onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@Composable
private fun MapTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 뒤로가기 버튼
        Surface(
            onClick = onBack,
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 2.dp,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.search_ic_arrow_back),
                    contentDescription = "뒤로",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF333333)
                )
            }
        }

        // 검색바
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 2.dp,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.search_ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color(0xFFBBBBBB)
                )
                Text(
                    text = "봉사명, 기관, 지역 검색",
                    fontSize = 13.sp,
                    color = Color(0xFFBBBBBB)
                )
            }
        }

        // 필터 버튼
        Surface(
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 2.dp,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.map_ic_filter),
                    contentDescription = "필터",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF555555)
                )
            }
        }
    }
}

@Composable
private fun MapFilterRow(
    selectedFilter: String?,
    onFilterClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterChips.forEach { label ->
            val isActive = selectedFilter == label
            val chipColor = categoryColors[label] ?: GreenPrimary
            Surface(
                onClick = { onFilterClick(label) },
                shape = CircleShape,
                color = if (isActive) chipColor else Color.White,
                border = BorderStroke(1.dp, if (isActive) chipColor else CardBorder),
                shadowElevation = 2.dp
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive) Color.White else Color(0xFF666666),
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)
                )
            }
        }
    }
}

@Composable
private fun MapArea(selectedFilter: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 지도 배경
        Image(
            painter = painterResource(R.drawable.map_map_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 지도 핀들
        mapPins.forEach { pin ->
            val isSelected = selectedFilter != null && pin.label == selectedFilter
            MapPinChip(pin = pin, isSelected = isSelected, modifier = Modifier.offset(x = pin.x, y = pin.y))
        }

        // 내 위치 파란 점
        Box(
            modifier = Modifier
                .offset(x = 143.dp, y = 208.dp)
                .size(24.dp)
                .background(Color(0xFF4285F4), CircleShape)
        )

        // 이 지역 봉사 N개 라벨
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)) {
                Text("이 지역 봉사 ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text("7개", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }
        }

        // 나침반 버튼 (우하단)
        Surface(
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.map_ic_compass),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = GreenPrimary
                )
            }
        }
    }
}

@Composable
private fun MapPinChip(pin: MapPin, isSelected: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) pin.color else Color.White,
        border = BorderStroke(1.5.dp, pin.color),
        shadowElevation = if (isSelected) 6.dp else 2.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(if (isSelected) Color.White else pin.color, CircleShape)
            )
            Text(
                text = pin.label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else pin.color
            )
        }
    }
}

@Composable
private fun MapBottomSheet(onNavigateToDetail: () -> Unit = {}) {
    Surface(
        color = Color.White,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // 드래그 핸들
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
            }

            // 봉사 카드
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 썸네일
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFE8F2E3),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🌿", fontSize = 22.sp)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    // 태그 + 거리
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE8F2E3)
                        ) {
                            Text(
                                "환경",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            "서울 마포구 · 0.4km",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFAAAAAA)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "홍제천 생태 환경 정화",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("📅 7월 5일 (토)", fontSize = 11.sp, color = Color(0xFF888888))
                        Text("⏱ 3시간", fontSize = 11.sp, color = Color(0xFF888888))
                    }
                }

                Icon(
                    painter = painterResource(R.drawable.home_ic_heart),
                    contentDescription = "찜",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFCCCCCC)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 상세 보기 버튼
            Button(
                onClick = onNavigateToDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("상세 보기 →", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    VodaTheme {
        MapScreen()
    }
}
