package com.mesh.voda.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        // 일러스트 영역 (좌하단에 "일러스트 N/4" 라벨)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f)
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
                Text(
                    text = "일러스트 ${index + 1}/$total",
                    fontSize = 11.sp,
                    color = OnboardingGrayText
                )
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