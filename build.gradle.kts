// build.gradle.kts (Project level)
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // <-- Updated version
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

// Add this block to configure the Java toolchain

// Optional: If you need to add dependencies to the buildscript
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}