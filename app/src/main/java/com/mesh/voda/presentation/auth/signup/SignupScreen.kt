package com.mesh.voda.presentation.auth.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mesh.voda.data.auth.model.Interest
import com.mesh.voda.data.auth.model.KoreaRegions
import com.mesh.voda.data.auth.model.Region
import com.mesh.voda.presentation.auth.login.WireframeGrayText
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset

// 디자인 상수
val VoDaBlack = Color(0xFF1C1C1E)
val VoDaGrayText = Color(0xFF8E8E93)
val VoDaLightGray = Color(0xFFE5E5EA)
val VoDaBg = Color(0xFFFFFFFF)
// 지역 직접 설정 팝업 색상
val RegionSelectGray = Color(0xFFF0F0F0) // 시·도/구·군 선택 시
val RegionChipGray = Color(0xFFE8E8E8)   // 하단 선택된 지역 칩
val RegionFieldGray = Color(0xFFF0F0F0)  // 검색창 배경
val VoDaTermsBg = Color(0xFFF9F9F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupComplete: () -> Unit,
    onBack: () -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) onSignupComplete()
    }

    BackHandler { if (viewModel.onBack()) onBack() }

    val stepIndex = uiState.step.ordinal
    val totalSteps = SignupStep.entries.size

    Scaffold(containerColor = VoDaCreamBg) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(VoDaCreamBg)
        ) {
            StepIndicator(
                current = stepIndex,
                total = totalSteps,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            if (uiState.step == SignupStep.BASIC_INFO) {
                SignupHeader(onBackClick = { if (viewModel.onBack()) onBack() })
            } else {
                SignupBackButton(onClick = { if (viewModel.onBack()) onBack() })
            }

            Box(modifier = Modifier.weight(1f)) {
                when (uiState.step) {
                    SignupStep.BASIC_INFO -> BasicInfoStep(uiState, viewModel)
                    SignupStep.INTERESTS -> InterestsStep(uiState, viewModel)
                    SignupStep.TENDENCY -> TendencyStep(uiState, viewModel)
                }
            }

            if (uiState.isRegionPickerOpen) {
                RegionPickerSheet(uiState, viewModel)
            }

            uiState.errorMessage?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Button(
                onClick = viewModel::onNext,
                enabled = uiState.canProceed,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VoDaSageGreen,
                    disabledContainerColor = VoDaLightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(54.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                } else {
                    Text(
                        when (uiState.step) {
                            SignupStep.TENDENCY -> "내 봉사 유형 보기 ✨"
                            else -> "다음"
                        },
                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White
                    )
                    if (uiState.step == SignupStep.BASIC_INFO || uiState.step == SignupStep.INTERESTS) {
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// 단계 화면 상단 좌측 뒤로가기 버튼 (이전 단계로, 첫 단계면 화면을 닫음)
@Composable
private fun SignupBackButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp)) {
        IconButton(onClick = onClick, modifier = Modifier.size(32.dp).offset(x = (-6).dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = VoDaBlack, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun SignupHeader(onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(32.dp).offset(x = (-6).dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = VoDaBlack, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text("프로필을 설정해주세요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
        Spacer(Modifier.height(4.dp))
        Text("Google 계정 정보로 자동으로 채워드렸어요", fontSize = 14.sp, color = VoDaGrayText)
    }
}

private fun nextStepName(step: SignupStep): String? = when (step) {
    SignupStep.BASIC_INFO -> "관심 분야"
    SignupStep.INTERESTS -> "봉사 성향"
    SignupStep.TENDENCY -> null
}

@Composable
private fun StepIndicator(current: Int, total: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(VoDaLightGray)
        ) {
            repeat(total) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp)
                        .background(if (index <= current) VoDaSageGreen else Color.Transparent)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${current + 1}/${total}단계", fontSize = 12.sp, color = VoDaGrayText)
            nextStepName(SignupStep.entries[current])?.let {
                Text(it, fontSize = 12.sp, color = VoDaGrayText)
            }
        }
    }
}

@Composable
private fun StepTitle(title: String, description: String) {
    Column {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
        Spacer(Modifier.height(8.dp))
        Text(description, fontSize = 14.sp, color = VoDaGrayText)
    }
}

/* ---------------------------- Step 1: 프로필 설정 ---------------------------- */

// 아바타 이모지. selectedAvatar 인덱스가 이 리스트를 가리킴.
private val avatarEmojis = listOf("🌱", "🌿", "🍀", "🌳", "🐾", "🍱", "🐥", "⭐")

@Composable
private fun BasicInfoStep(state: SignupUiState, viewModel: SignupViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // 대표 아바타 + 카메라 배지
        Box(modifier = Modifier.align(Alignment.CenterHorizontally), contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(VoDaSageGreen.copy(alpha = 0.1f))
                    .border(3.dp, VoDaSageGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(avatarEmojis.getOrElse(state.selectedAvatar) { "🌱" }, fontSize = 44.sp)
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(VoDaSageGreen)
                    .border(2.dp, VoDaCreamBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "사진 변경", tint = Color.White, modifier = Modifier.size(15.dp))
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "탭해서 사진 변경",
            fontSize = 12.sp,
            color = VoDaGrayText,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(28.dp))
        Text("또는 아바타 선택", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = VoDaGrayText)
        Spacer(Modifier.height(12.dp))
        AvatarGrid(selected = state.selectedAvatar, onSelect = viewModel::selectAvatar)

        Spacer(Modifier.height(28.dp))
        // 닉네임
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("닉네임", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VoDaBlack, modifier = Modifier.weight(1f))
            Text("2-10자", fontSize = 12.sp, color = VoDaGrayText)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.nickname,
            onValueChange = viewModel::onNicknameChange,
            placeholder = { Text("닉네임을 입력해주세요", color = VoDaGrayText, fontSize = 14.sp) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            //colors = textFieldColors(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = VoDaSageGreen,
                focusedBorderColor = VoDaSageGreen,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = { GoogleBadge("자동입력") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Text("나중에 마이페이지에서 변경할 수 있어요", fontSize = 12.sp, color = VoDaGrayText)

        Spacer(Modifier.height(20.dp))

        Text("이메일", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.email,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = VoDaLightGray,
                disabledContainerColor = VoDaCreamBg,
                disabledTextColor = VoDaGrayText
            ),
            trailingIcon = { GoogleBadge("Google 연동") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable private fun FieldLabel(text: String) = Text(text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VoDaBlack, modifier = Modifier.padding(bottom = 8.dp))
@Composable private fun textFieldColors() = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = VoDaLightGray, focusedBorderColor = VoDaBlack, unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)


@Composable
private fun GoogleBadge(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Image(
            painter = painterResource(com.mesh.voda.R.drawable.ic_google_logo),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 12.sp, color = VoDaGrayText)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AvatarGrid(selected: Int, onSelect: (Int) -> Unit) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        avatarEmojis.indices.chunked(4).forEach { rowIndices ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowIndices.forEach { index ->
                    AvatarCell(
                        emoji = avatarEmojis[index],
                        selected = index == selected,
                        onClick = { onSelect(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
private fun AvatarCell(emoji: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1.25f) // 가로로 살짝 납작한 직사각형
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) VoDaSageGreen.copy(alpha = 0.15f) else Color.White)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) VoDaSageGreen else VoDaLightGray,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 26.sp)
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(VoDaSageGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = "선택됨", tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
    }
}

/* ---------------------------- Step 2: 관심 분야 ---------------------------- */
/* ---------------------------- Step 2: 관심 분야 ---------------------------- */

private val interestEmojis: Map<Interest, String> = mapOf(
    Interest.ENVIRONMENT to "🌿",
    Interest.CHILDREN to "📚",
    Interest.SENIOR to "🧓",
    Interest.ANIMAL to "🐾",
    Interest.DISABILITY to "♿",
    Interest.HEALTH to "🏥",
    Interest.MULTICULTURE to "🌍",
    Interest.COMMUNITY to "🏘️",
    Interest.CULTURE to "🎨",
)

@Composable
private fun InterestsStep(state: SignupUiState, viewModel: SignupViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(bottom = 20.dp)) {
                Text("관심 분야를 골라주세요", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
                Spacer(Modifier.height(6.dp))
                Row {
                    Text("최대 ${SignupViewModel.MAX_INTERESTS}개까지 선택할 수 있어요 · ", fontSize = 13.sp, color = VoDaGrayText)
                    Text(
                        "${state.interests.size}개 선택됨",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VoDaSageGreen
                    )
                }
            }
        }
        items(Interest.entries) { interest ->
            InterestCard(
                interest = interest,
                selected = interest in state.interests,
                onClick = { viewModel.toggleInterest(interest) }
            )
        }
    }
}

@Composable
private fun InterestCard(interest: Interest, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) VoDaSageGreen else Color.White)
            .border(1.dp, if (selected) Color.Transparent else VoDaLightGray, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp),
                    //.clip(CircleShape)
                    //.background(if (selected) Color.White.copy(alpha = 0.25f) else VoDaLightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(interestEmojis[interest].orEmpty(), fontSize = 22.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = interest.label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.White else VoDaBlack,
                textAlign = TextAlign.Center
            )
        }
        // 선택 체크 배지
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "선택됨",
                    tint = VoDaSageGreen,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
/* ---------------------------- Step 3: 봉사 성향 (전체 UI 구현) ---------------------------- */

val VoDaSageGreen = Color(0xFF4E8A3F)
val VoDaCreamBg = Color(0xFFFBF7EF)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TendencyStep(state: SignupUiState, viewModel: SignupViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(VoDaCreamBg)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        // 타이틀
        Text("봉사 성향을 알려주세요", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
        Spacer(Modifier.height(8.dp))
        Text("나에게 맞는 봉사를 더 잘 추천해드릴게요", fontSize = 14.sp, color = VoDaGrayText)

        Spacer(Modifier.height(32.dp))

        // 1. 활동 가능 시간
        QuestionTitle("언제 활동하기 좋으세요?")
        FlowRow(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("평일 오전", "평일 오후", "주말", "상관없음").forEach { time ->
                TendencyChip(text = time, selected = state.activityTime == time) { viewModel.selectActivityTime(time) }
            }
        }

        Spacer(Modifier.height(32.dp))

        // 2. 참여 빈도
        QuestionTitle("얼마나 자주 참여하고 싶으세요?")
        FlowRow(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("월 1회", "월 2~3회", "주 1회 이상", "비정기").forEach { freq ->
                TendencyChip(text = freq, selected = state.activityFrequency == freq) { viewModel.selectActivityFrequency(freq) }
            }
        }

        Spacer(Modifier.height(32.dp))

        // 3. 선호 스타일 — 장소(실내/실외)와 인원(혼자/단체)은 각각 단일 선택
        QuestionTitle("어떤 스타일을 선호하세요?")
        Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("실내 활동", "실외 활동").forEach { place ->
                TendencyChip(text = place, selected = state.activityPlace == place) { viewModel.selectActivityPlace(place) }
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("혼자 참여", "단체 참여").forEach { groupType ->
                TendencyChip(text = groupType, selected = state.activityGroupType == groupType) { viewModel.selectActivityGroupType(groupType) }
            }
        }

        // 중간 점선 구분선
        DashedDivider(modifier = Modifier.padding(vertical = 24.dp))

        // 4. 활동 지역
        QuestionTitle("활동 지역은 어떻게 할까요?")
        Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val regions = listOf("내 주변", "지역 직접 설정", "온라인")
            regions.forEach { region ->
                TendencyChip(
                    text = region,
                    selected = state.activityRegionMethod == region,
                    icon = if (region == "내 주변") Icons.Default.MyLocation else null
                ) { viewModel.selectActivityRegionMethod(region) }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun QuestionTitle(text: String) {
    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
}

// 점선 구분선 구현
@Composable
private fun DashedDivider(modifier: Modifier = Modifier) {
    val color = VoDaLightGray
    Box(modifier = modifier.fillMaxWidth().height(1.dp).drawBehind {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = size.height,
            pathEffect = pathEffect
        )
    })
}

// 인터랙티브 칩 컴포넌트
@Composable
private fun TendencyChip(
    text: String,
    selected: Boolean,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        border = if (selected) null else BorderStroke(1.dp, VoDaLightGray),
        color = if (selected) VoDaSageGreen else Color.White,
        modifier = Modifier.height(42.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, null, modifier = Modifier.size(16.dp).padding(end = 4.dp), tint = if (selected) Color.White else VoDaBlack)
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.White else VoDaBlack
            )
        }
    }
}
/* ---------------------------- 활동 지역 직접 설정 (팝업) ---------------------------- */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun RegionPickerSheet(state: SignupUiState, viewModel: SignupViewModel) {
    var query by remember { mutableStateOf("") }
    val sidoList = Region.entries.filter { query.isBlank() || it.label.contains(query.trim()) }
    val districts = KoreaRegions.districtsBySido[state.selectedSido].orEmpty()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = viewModel::closeRegionPicker,
        sheetState = sheetState,
        containerColor = VoDaCreamBg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("지역 직접 설정", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VoDaBlack)
                    Spacer(Modifier.height(4.dp))
                    Text("시/도 선택 후 세부 지역을 골라주세요", fontSize = 13.sp, color = VoDaGrayText)
                }
                IconButton(onClick = viewModel::closeRegionPicker) {
                    Icon(Icons.Default.Close, contentDescription = "닫기", tint = VoDaGrayText)
                }
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("시/도 검색", color = VoDaGrayText, fontSize = 14.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = RegionFieldGray,
                    focusedContainerColor = RegionFieldGray,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = VoDaBlack.copy(alpha = 0.35f),
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                // 좌측: 시·도 목록 (선택 시 회색 배경)
                LazyColumn(modifier = Modifier.width(76.dp).fillMaxHeight()) {
                    items(sidoList) { sido ->
                        val selected = sido == state.selectedSido
                        Text(
                            text = sido.label,
                            fontSize = 14.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) VoDaBlack else VoDaGrayText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selected) RegionSelectGray else Color.Transparent)
                                .clickable { viewModel.selectSido(sido) }
                                .padding(vertical = 10.dp)
                        )
                    }
                }
                VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = VoDaLightGray)
                // 우측: 구·군 (3열 그리드, 가나다순, 8행가량 노출)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(districts.sorted()) { gugun ->
                        val full = KoreaRegions.fullName(state.selectedSido, gugun)
                        val selected = full in state.regions
                        Surface(
                            onClick = { viewModel.toggleDistrict(gugun) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) RegionSelectGray else Color.White,
                            border = BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) VoDaBlack else VoDaLightGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                gugun,
                                fontSize = 13.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = VoDaBlack,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 9.dp)
                            )
                        }
                    }
                }
            }

            if (state.regions.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = VoDaLightGray)
                Spacer(Modifier.height(16.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.regions.forEach { full ->
                        Surface(
                            onClick = { viewModel.removeRegion(full) },
                            shape = RoundedCornerShape(8.dp),
                            color = RegionChipGray
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 7.dp, bottom = 7.dp)
                            ) {
                                Text(full, fontSize = 13.sp, color = VoDaBlack)
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Default.Close, contentDescription = "삭제", tint = VoDaGrayText, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = viewModel::closeRegionPicker,
                enabled = state.regions.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VoDaSageGreen, disabledContainerColor = VoDaLightGray),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    if (state.regions.isEmpty()) "지역을 선택해주세요" else "${state.regions.size}개 지역 선택 완료",
                    fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
