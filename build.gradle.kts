// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    val room_version = "2.6.1"
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("androidx.room") version "$room_version" apply false
}