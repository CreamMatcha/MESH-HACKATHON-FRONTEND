# VoDa - 봉사 매칭 서비스

봉사 활동을 쉽고 빠르게 찾을 수 있는 AI 기반 봉사 매칭 Android 앱

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Navigation | Navigation Compose |
| Network | Retrofit2 + OkHttp3 |
| Image | Coil |

---

## 실행 방법

**요구사항**
- Android Studio Ladybug (2024.2.1) 이상
- JDK 11
- Android SDK 35

```bash
git clone https://github.com/CreamMatcha/MESH-HACKATHON-FRONTEND.git
cd MESH-HACKATHON-FRONTEND
```

Android Studio에서 프로젝트 열기 → Gradle Sync → Run

---

## 프로젝트 구조

```
app/src/main/java/com/mesh/voda/
├── di/                        # Hilt 의존성 주입
├── navigation/                # NavGraph, Screen 정의
├── data/
│   ├── remote/                # API 서비스, DTO
│   └── repository/            # Repository 구현체
├── domain/
│   ├── model/                 # 도메인 모델
│   └── repository/            # Repository 인터페이스
└── presentation/
    ├── common/theme/          # 테마, 타이포그래피
    ├── onboarding/            # 온보딩
    ├── auth/                  # 로그인, 회원가입
    ├── main/                  # 바텀 네비게이션
    ├── home/                  # 홈
    ├── search/                # 검색
    ├── saved/                 # 찜
    ├── activity/              # 내 활동
    └── settings/              # 마이페이지
```

---

## 브랜치 전략

```
main    ← 배포용 (PR만 머지 가능)
└── dev ← 통합 브랜치
    ├── feature/auth-login
    ├── feature/home-search
    └── feature/saved-activity
```

- 작업 단위로 `feature/*` 브랜치 생성
- `feature/* → dev` PR 생성 후 리뷰 1명 승인 → 머지
- `dev → main`은 마일스톤 완료 시

---

## 파트 분배

| 담당자 | 화면 |
|--------|------|
| A | 온보딩, 로그인, 회원가입 |
| B | 홈, 검색, 필터 |
| C | 찜, 내 활동, 마이페이지 |
