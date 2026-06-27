package com.mesh.voda.presentation.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 외부 데이터 모델 의존성을 제거하기 위해 UI 내부용 임시 데이터 구조 정의
data class MockInterest(val emoji: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToMbti: () -> Unit = {},
    onNavigateToInterests: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    // 다이얼로그 상태 및 스위치 상태를 UI 내부에서 자체 관리
    val showLogoutDialog = remember { mutableStateOf(false) }
    val showDeleteAccountDialog = remember { mutableStateOf(false) }

    val pushEnabled = remember { mutableStateOf(true) }
    val emailEnabled = remember { mutableStateOf(false) }
    val smsEnabled = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "마이페이지",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 프로필 카드
            ProfileCard(
                name = "홍길동",
                email = "voda@example.com",
                mbtiType = "ENFJ",
                totalCount = 12,
                totalHours = 48,
                gradeEmoji = "🥇",
                gradeLabel = "우수 봉사자",
                onEditClick = onNavigateToEditProfile
            )

            // 내 봉사 프로필
            SectionLabel("내 봉사 프로필")
            VolunteerProfileCard(
                mbtiType = "ENFJ",
                lastMbtiDate = "2026.03.15",
                interests = listOf(
                    MockInterest("🌿", "환경·생태"),
                    MockInterest("🐕", "동물보호")
                ),
                onMbtiClick = onNavigateToMbti,
                onInterestsClick = onNavigateToInterests
            )

            // 알림
            SectionLabel("알림")
            NotificationCard(
                pushEnabled = pushEnabled.value,
                emailEnabled = emailEnabled.value,
                smsEnabled = smsEnabled.value,
                onDetailClick = {},
                onPushToggle = { pushEnabled.value = it },
                onEmailToggle = { emailEnabled.value = it },
                onSmsToggle = { smsEnabled.value = it }
            )

            // 앱 정보
            SectionLabel("앱 정보")
            AppInfoCard(
                onAppSettingsClick = {},
                onPrivacyPolicyClick = {},
                onTermsClick = {},
                onSupportClick = {},
                onRateAppClick = {}
            )

            // 로그아웃
            LogoutButton(onClick = { showLogoutDialog.value = true })

            // 회원탈퇴 + 버전
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "회원 탈퇴",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.clickable { showDeleteAccountDialog.value = true }
                )
                Text(
                    text = "VoDa v1.0.0",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        // Dialogs 상태 분기
        if (showLogoutDialog.value) {
            ConfirmDialog(
                title = "로그아웃",
                message = "로그아웃 하시겠어요?",
                confirmText = "로그아웃",
                onConfirm = {
                    showLogoutDialog.value = false
                    onLogout()
                },
                onDismiss = { showLogoutDialog.value = false }
            )
        }
        if (showDeleteAccountDialog.value) {
            ConfirmDialog(
                title = "회원 탈퇴",
                message = "정말 탈퇴하시겠어요?\n모든 봉사 기록이 삭제됩니다.",
                confirmText = "탈퇴",
                isDestructive = true,
                onConfirm = {
                    showDeleteAccountDialog.value = false
                    onDeleteAccount()
                },
                onDismiss = { showDeleteAccountDialog.value = false }
            )
        }
    }
}

// ─── 프로필 카드 ────────────────────────────────────────────────────────────────

@Composable
private fun ProfileCard(
    name: String,
    email: String,
    mbtiType: String,
    totalCount: Int,
    totalHours: Int,
    gradeEmoji: String,
    gradeLabel: String,
    onEditClick: () -> Unit
) {
    SurfaceCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 프로필 사진
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    MbtiBadge(mbtiType = mbtiType)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = email,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                // 수정 버튼
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onEditClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "프로필 수정",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "수정",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // 통계
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "${totalCount}회", label = "누적 봉사")
                StatDivider()
                StatItem(value = "${totalHours}h", label = "활동 시간")
                StatDivider()
                StatItem(value = "$gradeEmoji $gradeLabel", label = "봉사 등급")
            }
        }
    }
}

@Composable
private fun MbtiBadge(mbtiType: String) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "🌿 $mbtiType",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .height(32.dp)
            .width(1.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    )
}

// ─── 봉사 프로필 카드 ───────────────────────────────────────────────────────────

@Composable
private fun VolunteerProfileCard(
    mbtiType: String,
    lastMbtiDate: String,
    interests: List<MockInterest>,
    onMbtiClick: () -> Unit,
    onInterestsClick: () -> Unit
) {
    SurfaceCard(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        MenuRow(
            icon = Icons.Default.Settings,
            title = "내 봉사 MBTI",
            subtitle = "마지막 분석: $lastMbtiDate",
            onClick = onMbtiClick
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🌿", fontSize = 20.sp)
                Column {
                    Text(
                        text = mbtiType,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "환경·생태 분야와 잘 맞아요",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onMbtiClick() }
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "성향\n재분석",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
        )
        Spacer(Modifier.height(4.dp))

        MenuRow(
            icon = Icons.Default.Person,
            title = "관심 분야 관리",
            subtitle = "현재 ${interests.size}개 분야 선택됨",
            onClick = onInterestsClick
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interests.forEach { category ->
                InterestTag(category = category)
            }
            AddTag()
        }
    }
}

@Composable
private fun InterestTag(category: MockInterest) {
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(category.emoji, fontSize = 13.sp)
        Text(category.name, fontSize = 13.sp)
        Text("✓", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun AddTag() {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text("+ 추가", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    }
}

// ─── 알림 카드 ─────────────────────────────────────────────────────────────────

@Composable
private fun NotificationCard(
    pushEnabled: Boolean,
    emailEnabled: Boolean,
    smsEnabled: Boolean,
    onDetailClick: () -> Unit,
    onPushToggle: (Boolean) -> Unit,
    onEmailToggle: (Boolean) -> Unit,
    onSmsToggle: (Boolean) -> Unit
) {
    SurfaceCard(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        MenuRow(
            icon = Icons.Default.Notifications,
            title = "알림 설정",
            subtitle = "수신 방법 및 알림 종류 설정",
            onClick = onDetailClick
        )
        Spacer(Modifier.height(4.dp))
        NotificationToggleRow(
            title = "푸시 알림",
            subtitle = "봉사 승인·마감 알림",
            checked = pushEnabled,
            onToggle = onPushToggle
        )
        NotificationToggleRow(
            title = "이메일 알림",
            subtitle = "활동 내역 요약",
            checked = emailEnabled,
            onToggle = onEmailToggle
        )
        NotificationToggleRow(
            title = "SMS 알림",
            subtitle = "긴급 공지",
            checked = smsEnabled,
            onToggle = onSmsToggle
        )
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

// ─── 앱 정보 카드 ──────────────────────────────────────────────────────────────

@Composable
private fun AppInfoCard(
    onAppSettingsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onSupportClick: () -> Unit,
    onRateAppClick: () -> Unit
) {
    SurfaceCard(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        MenuRow(
            icon = Icons.Default.Settings,
            title = "앱 설정",
            onClick = onAppSettingsClick
        )
        SimpleMenuRow(title = "개인정보 처리방침", onClick = onPrivacyPolicyClick)
        SimpleMenuRow(title = "이용약관", onClick = onTermsClick)
        SimpleMenuRow(title = "고객센터 / 문의", onClick = onSupportClick)
        SimpleMenuRow(title = "앱 평가하기", onClick = onRateAppClick, showDivider = false)
    }
}

// ─── 로그아웃 버튼 ─────────────────────────────────────────────────────────────

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "↪ 로그아웃",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─── 공통 컴포넌트 ─────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun SurfaceCard(
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun SimpleMenuRow(
    title: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            )
        }
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = if (isDestructive) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}