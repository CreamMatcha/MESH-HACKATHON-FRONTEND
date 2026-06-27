package com.mesh.voda.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

val OnboardingGreen = Color(0xFF4E8A3F)
val OnboardingCream = Color(0xFFFBF7EF)
val OnboardingPlaceholderGray = Color(0xFFE8E6E0)
val OnboardingTextBlack = Color(0xFF1C1C1E)
val OnboardingGrayText = Color(0xFF8E8E93)

// 일러스트용 색상
private val IllGreenTop = Color(0xFFDDEACF)
private val IllPinkTop = Color(0xFFF3DFDF)
private val IllBorder = Color(0xFFECEAE4)
private val IllGreenCircle = Color(0xFFE3F0DA)
private val IllPinkChipBg = Color(0xFFF7E3E8)
private val IllPinkText = Color(0xFFC76B86)
private val IllPurpleChipBg = Color(0xFFEAE4F3)
private val IllPurpleText = Color(0xFF7B6BA6)

private data class OnboardingPage(val title: String, val description: String)

private val pages = listOf(
    OnboardingPage("봉사, 이제는 취미처럼", "관심사와 성향에 맞는 봉사활동을 추천받고 의미 있는 경험을 시작해보세요."),
    OnboardingPage("AI가 딱 맞는 봉사를 추천해요", "환경, 아동, 노인, 동물보호 등 관심 있는 분야를 선택하면 나에게 맞는 봉사를 찾아드립니다."),
    OnboardingPage("한눈에 보는 봉사 일정", "신청한 봉사를 캘린더에서 확인하고 놓치지 않도록 알림을 받아보세요."),
    OnboardingPage("나만의 봉사 MBTI를 찾아보세요", "간단한 테스트를 통해 나에게 잘 맞는 봉사 스타일을 발견하고 더 즐겁게 참여해요."),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    val finish: () -> Unit = {
        viewModel.completeOnboarding()
        onFinish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingCream)
            .statusBarsPadding() // 상태바 영역 확보
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(pages[page], page, pages.size)
        }

        // 하단 인디케이터 (점)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp), // 버튼과의 간격 확보
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val selected = pagerState.currentPage == index
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (selected) 24.dp else 8.dp, 8.dp),
                    shape = RoundedCornerShape(50),
                    color = if (selected) OnboardingGreen else OnboardingPlaceholderGray
                ) {}
            }
        }

        // 하단 다음/시작하기 버튼
        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    finish()
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OnboardingGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp)
        ) {
            val isLast = pagerState.currentPage == pages.size - 1
            Text(if (isLast) "시작하기" else "다음", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            if (!isLast) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage, index: Int, total: Int) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(20.dp))

        // 일러스트 영역 — 2,3번째 화면은 실제 미리보기, 나머지는 placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f),
            contentAlignment = Alignment.Center
        ) {
            when (index) {
                1 -> AiRecommendIllustration()
                2 -> ActivityTimelineIllustration()
                else -> PlaceholderIllustration(index, total)
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 33.sp,
            color = OnboardingTextBlack
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = page.description,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = OnboardingGrayText
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PlaceholderIllustration(index: Int, total: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingPlaceholderGray, RoundedCornerShape(24.dp))
    ) {
        // TODO: 실제 일러스트 이미지 삽입 위치
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text("일러스트 ${index + 1}/$total", fontSize = 11.sp, color = OnboardingGrayText)
        }
    }
}

/* ---------------- 2번째 화면: AI 맞춤 추천 미리보기 ---------------- */

@Composable
private fun AiRecommendIllustration() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MiniRecCard(IllGreenTop, "🌱", "한강 플로깅 환경 캠페인", "서울 마포구", "7월 12일 (토)", Modifier.weight(1f))
        MiniRecCard(IllPinkTop, "🐾", "유기견 산책 & 미용 봉사", "경기 남양주", "7월 13일 (일)", Modifier.weight(1f))
    }
}

@Composable
private fun MiniRecCard(
    topColor: Color, emoji: String, name: String, location: String, date: String, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, IllBorder, RoundedCornerShape(14.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(74.dp).background(topColor)) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(OnboardingGreen)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text("+ AI 추천", fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Icon(
                Icons.Default.FavoriteBorder, null, tint = OnboardingGrayText,
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(13.dp)
            )
            Text(emoji, fontSize = 26.sp, modifier = Modifier.align(Alignment.Center))
        }
        Column(modifier = Modifier.padding(10.dp)) {
            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnboardingTextBlack, lineHeight = 15.sp)
            Spacer(Modifier.height(6.dp))
            MiniInfoRow(Icons.Default.Place, location)
            Spacer(Modifier.height(3.dp))
            MiniInfoRow(Icons.Default.CalendarMonth, date)
        }
    }
}

@Composable
private fun MiniInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = OnboardingGrayText, modifier = Modifier.size(11.dp))
        Spacer(Modifier.width(3.dp))
        Text(text, fontSize = 10.sp, color = OnboardingGrayText)
    }
}

/* ---------------- 3번째 화면: 활동 이력 미리보기 ---------------- */

@Composable
private fun ActivityTimelineIllustration() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, IllBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // 탭
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            TabItem("신청한 봉사", "2", false)
            TabItem("완료한 봉사", "7", false)
            TabItem("활동 이력", "12", true)
        }
        Spacer(Modifier.height(12.dp))
        // 카테고리 칩
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CategoryChip("🌱 환경 7회", OnboardingGreen, Color.White)
            CategoryChip("💗 복지 3회", IllPinkChipBg, IllPinkText)
            CategoryChip("🎓 교육 2회", IllPurpleChipBg, IllPurpleText)
        }
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("2025년", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnboardingTextBlack, modifier = Modifier.weight(1f))
            Text("10회", fontSize = 10.sp, color = OnboardingGrayText)
        }
        Spacer(Modifier.height(8.dp))
        TimelineItem("마포구 홍제천 생태 환경 정화", "2025년 6월 21일")
        Spacer(Modifier.height(8.dp))
        TimelineItem("한강 플로깅 환경 캠페인", "2025년 5월 3일")
    }
}

@Composable
private fun TabItem(label: String, count: String, selected: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) OnboardingTextBlack else OnboardingGrayText
        )
        Spacer(Modifier.width(3.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (selected) OnboardingGreen else OnboardingPlaceholderGray)
                .padding(horizontal = 5.dp, vertical = 1.dp)
        ) {
            Text(count, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else OnboardingGrayText)
        }
    }
}

@Composable
private fun CategoryChip(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

@Composable
private fun TimelineItem(title: String, date: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 타임라인 점
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .border(2.dp, OnboardingGreen, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        // 항목 카드
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(OnboardingCream)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(30.dp).clip(RoundedCornerShape(8.dp)).background(IllGreenCircle),
                contentAlignment = Alignment.Center
            ) { Text("🌱", fontSize = 15.sp) }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(IllGreenCircle)
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text("환경", fontSize = 8.sp, color = OnboardingGreen, fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.width(3.dp))
                    Icon(Icons.Default.Check, null, tint = OnboardingGreen, modifier = Modifier.size(10.dp))
                }
                Spacer(Modifier.height(3.dp))
                Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnboardingTextBlack, lineHeight = 13.sp)
                Text(date, fontSize = 8.sp, color = OnboardingGrayText)
            }
        }
    }
}