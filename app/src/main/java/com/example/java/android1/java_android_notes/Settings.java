package com.example.java.android1.java_android_notes;

public class Settings {

    public static final String SHARED_PREFERENCE_NAME = "Notes";
    public static final String KEY_IS_DARK_THEME = "Notes.KeyDarkTheme";
    public static final String KEY_IS_SYSTEM_THEME = "Notes.KeySystemTheme";
    public static final String KEY_TEXT_SIZE = "Notes.KeyTextSize";
    public static final String KEY_LAYOUT_VIEW = "Notes.KeyLayoutView";

    public static final String KEY_TEXT_SIZE_INDEX = "Notes.KeyTextSize.index";
    public static final String KEY_LAYOUT_VIEW_INDEX = "Notes.KeyLayoutView.index";

    public static boolean isDarkTheme;
    public static boolean isSystemTheme;
    public static int textSize;
    public static int layoutView;
    public static int textSizeIdx;
    public static int layoutViewIdx;

    public static final int SMALL_TEXT_SIZE = 10;
    public static final int MEDIUM_TEXT_SIZE = 20;
    public static final int LARGE_TEXT_SIZE = 30;

    public static final int GRID_LAYOUT_VIEW = 0;
    public static final int LINEAR_LAYOUT_VIEW = 1;
}
