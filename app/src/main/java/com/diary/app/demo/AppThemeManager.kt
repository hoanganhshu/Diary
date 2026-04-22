package com.diary.app.demo

object AppThemeManager {


    var backgroundImageRes: Int = R.drawable.security


    var primaryButtonColor: Int = 0xFF2196F3.toInt()


    var primaryTextColor: Int = 0xFF000000.toInt()

    fun applyTheme(
        backgroundRes: Int,
        buttonColor: Int,
        textColor: Int
    ) {
        backgroundImageRes = backgroundRes
        primaryButtonColor = buttonColor
        primaryTextColor = textColor
    }
}
