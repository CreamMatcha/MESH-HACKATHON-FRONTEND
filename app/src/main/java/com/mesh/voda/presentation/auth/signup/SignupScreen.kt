package com.mesh.voda.presentation.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO: 3단계 회원가입 (Step 1: 기본정보, Step 2: 관심 카테고리, Step 3: 활동 지역)
@Composable
fun SignupScreen(
    onSignupComplete: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("회원가입", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("구현 예정", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSignupComplete, modifier = Modifier.fillMaxWidth()) {
            Text("완료 (임시)")
        }
        TextButton(onClick = onBack) { Text("뒤로") }
    }
}
