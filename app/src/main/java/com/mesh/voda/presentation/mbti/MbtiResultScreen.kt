package com.mesh.voda.presentation.mbti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val Cream = Color(0xFFFBF7EF)
private val Green = Color(0xFF4E8A3F)
private val TextBlack = Color(0xFF1C1C1E)
private val GrayText = Color(0xFF8E8E93)
private val KeywordBg = Color(0xFFEAF3E4)
private val GreenTint = Color(0xFFE3F0DA)
private val PinkTint = Color(0xFFF6E2E2)
private val YellowTint = Color(0xFFFBEFCB)

// --- 가짜 데이터 (TODO: 백엔드에서 MBTI 결과 받아 교체) ---
private data class MbtiMatch(val emoji: String, val label: String, val circleColor: Color)

private data class MbtiResult(
    val avatarEmoji: String,
    val typeLabel: String,      // "GREEN · 그린"
    val typeName: String,       // "그린메이커형"
    val description: String,
    val keywords: List<String>,
    val matches: List<MbtiMatch>,
)

private val fakeResult = MbtiResult(
    avatarEmoji = "🐶",
    typeLabel = "GREEN · 그린",
    typeName = "그린메이커형",
    description = "말보다 행동이 먼저인 세상을 바꾸는 실행가",
    keywords = listOf("#실행력", "#야외 활동", "#체험형"),
    matches = listOf(
        MbtiMatch("🌱", "환경·생태", GreenTint),
        MbtiMatch("🐾", "동물보호", PinkTint),
        MbtiMatch("🤲", "지역사회", YellowTint),
    ),
)

@Composable
fun MbtiResultScreen(onSeeRecommendations: () -> Unit) {
    val result = fakeResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            ResultCard(result)

            Spacer(Modifier.height(28.dp))
            Text("나의 봉사 키워드", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                result.keywords.forEach { KeywordChip(it) }
            }

            Spacer(Modifier.height(24.dp))
            Text("이런 봉사가 잘 맞아요", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Spacer(Modifier.height(12.dp))
            result.matches.forEach { match ->
                MatchCard(match)
                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = onSeeRecommendations,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(54.dp)
        ) {
            Text("맞춤 봉사 보러가기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ResultCard(result: MbtiResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(vertical = 28.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("나의 봉사 MBTI는", fontSize = 13.sp, color = GrayText)
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(GreenTint),
            contentAlignment = Alignment.Center
        ) {
            // TODO: 실제 캐릭터 일러스트로 교체
            Text(result.avatarEmoji, fontSize = 56.sp)
        }
        Spacer(Modifier.height(16.dp))
        Text(result.typeLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Green, letterSpacing = 1.sp)
        Spacer(Modifier.height(6.dp))
        Text(result.typeName, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        Spacer(Modifier.height(10.dp))
        Text(
            result.description,
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun KeywordChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(KeywordBg)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Green)
    }
}

@Composable
private fun MatchCard(match: MbtiMatch) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(match.circleColor),
            contentAlignment = Alignment.Center
        ) {
            Text(match.emoji, fontSize = 20.sp)
        }
        Spacer(Modifier.width(14.dp))
        Text(match.label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextBlack)
    }
}
