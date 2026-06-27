package com.mesh.voda.presentation.common.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.mesh.voda.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val NotoSansKR = GoogleFont("Noto Sans KR")

val NotoSansKRFamily = FontFamily(
    Font(googleFont = NotoSansKR, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = NotoSansKR, fontProvider = googleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = NotoSansKR, fontProvider = googleFontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = NotoSansKR, fontProvider = googleFontProvider, weight = FontWeight.Bold),
)
