package com.example.interviewchallenge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)




val ColorScheme.AccentPurpleColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF4A64FC) else Color(0xff3D5AFE)



val ColorScheme.BackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF212121) else Color(0xffFFFFFF)



val ColorScheme.BlackWhiteColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffFFFFFF) else Color(0xff000000)


val ColorScheme.WhitePurple: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffFFFFFF) else Color(0xff3D5AFE)


val ColorScheme.GreenColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xff66BB6A) else Color(0xff4CAF50)


val ColorScheme.YellowColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffFFEB3B) else Color(0xffFFEB3B)



val ColorScheme.StrongYellowColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFB6A904) else Color(0xFFB6A904)


val ColorScheme.RedColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xffF44336) else Color(0xffF44336)



val ColorScheme.LightGray: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFD9D9D9) else Color(0xFFB0B0B0)


val ColorScheme.BoxGrayColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF3B3A3A) else Color(0xFFE7E4E4)



val ColorScheme.RedWhite: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFE3E2E2) else Color(0xFF670000)


val ColorScheme.BlueColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF64B5F6) else Color(0xFF2196F3)






































