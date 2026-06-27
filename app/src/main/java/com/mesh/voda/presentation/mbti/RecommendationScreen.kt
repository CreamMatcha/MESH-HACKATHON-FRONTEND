package com.mesh.voda.presentation.mbti

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
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

private val RecCream = Color(0xFFFBF7EF)
private val RecGreen = Color(0xFF4E8A3F)
private val RecBlack = Color(0xFF1C1C1E)
private val RecGray = Color(0xFF8E8E93)
private val RecBorder = Color(0xFFECEAE4)
private val GreenTop = Color(0xFFDDEACF)
private val PinkTop = Color(0xFFF3DFDF)
private val LikeRed = Color(0xFFEF5350)


private data class Recommendation(
    val name: String,
    val location: String,
    val date: String,
    val emoji: String,
    val topColor: Color,
)

private val fakeRecommendations = listOf(
    Recommendation("한강 플로깅 환경 캠페인", "서울 마포구", "7월 12일 (토)", "🌱", GreenTop),
    Recommendation("유기견 산책 & 미용 봉사", "경기 남양주", "7월 13일 (일)", "🐾", PinkTop),
    Recommendation("한강 플로깅 환경 캠페인", "서울 마포구", "7월 12일 (토)", "🌱", GreenTop),
    Recommendation("유기견 산책 & 미용 봉사", "경기 남양주", "7월 13일 (일)", "🐾", PinkTop),
    Recommendation("한강 플로깅 환경 캠페인", "서울 마포구", "7월 12일 (토)", "🌱", GreenTop),
    Recommendation("유기견 산책 & 미용 봉사", "경기 남양주", "7월 13일 (일)", "🐾", PinkTop),
)

@Composable
fun RecommendationScreen(onGoHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecCream)
            .statusBarsPadding()
    ) {
        Text(
            "AI 맞춤 추천",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RecBlack,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fakeRecommendations) { rec -> RecommendationCard(rec) }
        }

        Button(
            onClick = onGoHome,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RecGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(54.dp)
        ) {
            Text("홈 화면으로", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun RecommendationCard(rec: Recommendation) {

    var liked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, RecBorder, RoundedCornerShape(16.dp))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .background(rec.topColor)
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(RecGreen)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("+ AI 추천", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Icon(
                imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (liked) "찜 취소" else "찜",
                tint = if (liked) LikeRed else RecGray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable { liked = !liked }
                    .size(20.dp)
            )

            Text(rec.emoji, fontSize = 38.sp, modifier = Modifier.align(Alignment.Center))
        }


        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
            Text(
                rec.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RecBlack,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 19.sp
            )
            Spacer(Modifier.height(8.dp))
            InfoRow(Icons.Default.Place, rec.location)
            Spacer(Modifier.height(4.dp))
            InfoRow(Icons.Default.CalendarMonth, rec.date)
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = RecGray, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 12.sp, color = RecGray)
    }
}
