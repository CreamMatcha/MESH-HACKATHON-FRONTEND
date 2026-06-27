package com.mesh.voda.presentation.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesh.voda.R

// 와이어프레임 컬러
val WireframeBlack = Color(0xFF1C1C1E)
val WireframeGrayText = Color(0xFF8E8E93)
val WireframeBorder = Color(0xFFE5E5EA)
val WireframeBg = Color(0xFFFBF7EF)
val WireframeGreen = Color(0xFF4E8A3F)
val WireframePillBg = Color(0xFFEAF3E4)
private val TermsTextLight = Color(0xFFC4C2BC)
private val TermsTextMedium = Color(0xFF9E9C96)

@Composable
fun LoginScreen(
    onGoogleStart: () -> Unit,
    onSkip: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WireframeBg)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // 로고
        Image(
            painter = painterResource(id = R.drawable.home_ic_logo),
            contentDescription = "VoDa 로고",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("VoDa", fontSize = 34.sp, fontWeight = FontWeight.Black, color = WireframeBlack)
        Spacer(modifier = Modifier.height(10.dp))
        Text("나에게 꼭 맞는 봉사를 찾아드려요", fontSize = 14.sp, color = WireframeGrayText)

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(WireframePillBg)
                .border(1.dp, WireframeGreen.copy(alpha = 0.45f), RoundedCornerShape(50))
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Text("🌱 AI가 나의 봉사 유형을 분석해드려요", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WireframeGreen)
        }

        Spacer(modifier = Modifier.weight(0.9f))


        Button(
            onClick = onGoogleStart, // TODO: 실제 구글 로그인 SDK 연동 (현재 Mock)
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, WireframeBorder),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp
            )
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Google로 시작하기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = WireframeBlack)
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onSkip) {
            Text("로그인 없이 둘러보기", fontSize = 13.sp, color = WireframeGrayText)
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = buildAnnotatedString {
                append("계속 진행하면 ")
                withStyle(SpanStyle(color = TermsTextMedium, fontWeight = FontWeight.Medium)) { append("이용약관") }
                append(" 및 ")
                withStyle(SpanStyle(color = TermsTextMedium, fontWeight = FontWeight.Medium)) { append("개인정보 처리방침") }
                append("에 동의한 것으로 간주됩니다.")
            },
            fontSize = 11.sp,
            color = TermsTextLight,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.weight(1.6f))
    }
}
