package com.example.java.android1.java_android_notes

object Settings {
    const val SHARED_PREFERENCE_NAME = "Notes"
    const val KEY_IS_DARK_THEME = "Notes.KeyDarkTheme"
    const val KEY_IS_SYSTEM_THEME = "Notes.KeySystemTheme"
    const val KEY_TEXT_SIZE = "Notes.KeyTextSize"
    const val KEY_LAYOUT_VIEW = "Notes.KeyLayoutView"
    const val KEY_TEXT_SIZE_INDEX = "Notes.KeyTextSize.index"
    const val KEY_LAYOUT_VIEW_INDEX = "Notes.KeyLayoutView.index"
    @JvmField
    var isDarkTheme = false
    @JvmField
    var isSystemTheme = false
    @JvmField
    var textSize = 0
    @JvmField
    var layoutView = 0
    @JvmField
    var textSizeIdx = 0
    @JvmField
    var layoutViewIdx = 0
    const val SMALL_TEXT_SIZE = 10
    const val MEDIUM_TEXT_SIZE = 20
    const val LARGE_TEXT_SIZE = 30
    const val GRID_LAYOUT_VIEW = 0
    const val LINEAR_LAYOUT_VIEW = 1
}