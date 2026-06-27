package com.mesh.voda.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R
import com.mesh.voda.presentation.common.theme.VodaTheme

private val HeroGreen  = Color(0xFFD8EAC8)
private val GreenPrimary = Color(0xFF4E8A3F)
private val GreenLight = Color(0xFFEEF6E8)
private val GreenBorder = Color(0xFFC8DEB8)
private val CardBorder = Color(0xFFE0D8CA)

@Composable
fun DetailScreen(onBack: () -> Unit = {}) {
    var isSaved by remember { mutableStateOf(false) }

    // 흰색 배경: 스크롤 콘텐츠 아래도 크림색 없이 흰색으로 이어짐
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 헤로 + 스크롤 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                HeroSection(onBack = onBack)
                ContentSection()
                // 콘텐츠가 짧아도 흰색이 화면 끝까지 채워짐 (Box 배경이 White)
            }

            // 하단 고정 액션 버튼
            BottomActionBar(
                isSaved = isSaved,
                onSaveToggle = { isSaved = !isSaved }
            )
        }
    }
}

@Composable
private fun HeroSection(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(HeroGreen)
    ) {
        // 뒤로가기 버튼
        Surface(
            onClick = onBack,
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 2.dp,
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp)
                .size(36.dp)
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

        // 이모지 중앙
        Text(
            text = "🌱",
            fontSize = 90.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ContentSection() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-28).dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 태그 행
            TagRow()

            // 제목 + 기관
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "한강 플로깅 — 쓰레기 줍기 환경 캠페인",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = (-0.3).sp,
                    lineHeight = 28.sp
                )
                Text(
                    text = "서울환경연합",
                    fontSize = 13.sp,
                    color = Color(0xFF888888)
                )
            }

            // AI 매칭 카드
            AiMatchCard()

            // 정보 목록
            InfoSection()
        }
    }
}

@Composable
private fun TagRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // 모집중
        Surface(
            shape = CircleShape,
            color = Color(0xFFF0EDE2)
        ) {
            Text(
                "모집중",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF888888),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
        // 환경·생태
        Surface(
            shape = CircleShape,
            color = Color(0xFFE8F2E3),
            border = BorderStroke(1.dp, GreenPrimary)
        ) {
            Text(
                "환경·생태",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = GreenPrimary,
                modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
            )
        }
        // 마감 D-8
        Surface(
            shape = CircleShape,
            color = Color(0xFFFFF0E0)
        ) {
            Text(
                "마감 D-8",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFD07030),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun AiMatchCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = GreenLight,
        border = BorderStroke(1.dp, GreenBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 17.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 링 프로그레스 (94%)
            RingProgress(progress = 0.94f, label = "94%")

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "그린메이커형인 나와 잘 맞아요",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    "관심분야 일치 · 활동 스타일 · 가까운 거리",
                    fontSize = 11.sp,
                    color = Color(0xFF7AAA68)
                )
            }
        }
    }
}

@Composable
private fun RingProgress(progress: Float, label: String) {
    Box(
        modifier = Modifier.size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
            // 배경 트랙
            drawArc(
                color = Color(0xFFD8EAC8),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            // 진행 아크
            drawArc(
                color = GreenPrimary,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = stroke
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
        )
    }
}

@Composable
private fun InfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        InfoRow(iconRes = R.drawable.detail_ic_location, label = "활동 장소", value = "망원한강공원 1주차장 (마포구)")
        InfoRow(iconRes = R.drawable.detail_ic_calendar, label = "활동 일자", value = "2026.07.12 · 09:00~12:00")
        InfoRow(iconRes = R.drawable.detail_ic_clock,    label = "봉사 시간", value = "3시간 인정")
        RecruitRow()
    }
}

@Composable
private fun InfoRow(iconRes: Int, label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 라벨 (아이콘 + 텍스트 고정 너비)
        Row(
            modifier = Modifier.width(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = Color(0xFFAAAAAA)
            )
            Text(label, fontSize = 12.sp, color = Color(0xFFAAAAAA))
        }
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
    }
}

@Composable
private fun RecruitRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 라벨
        Row(
            modifier = Modifier.width(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.detail_ic_people),
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = Color(0xFFAAAAAA)
            )
            Text("모집 인원", fontSize = 12.sp, color = Color(0xFFAAAAAA))
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("21 / 30명", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
                Text("9자리 남음", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = GreenPrimary)
            }
            // 프로그레스 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0D8CA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(21f / 30f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(GreenPrimary)
                )
            }
        }
    }
}

@Composable
private fun BottomActionBar(isSaved: Boolean, onSaveToggle: () -> Unit) {
    Surface(
        color = Color.White,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(color = Color(0xFFFBF7EF), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 17.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 찜 버튼
            Surface(
                onClick = onSaveToggle,
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.5.dp, CardBorder),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            if (isSaved) R.drawable.home_ic_heart else R.drawable.home_ic_heart
                        ),
                        contentDescription = "찜",
                        modifier = Modifier.size(20.dp),
                        tint = if (isSaved) Color(0xFFE53935) else Color(0xFFCCCCCC)
                    )
                }
            }

            // 신청하러 가기 버튼
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    "신청하러 가기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.detail_ic_link),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    VodaTheme {
        DetailScreen()
    }
}
