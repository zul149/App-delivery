// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val agp_version1 by extra("8.1.2")
    repositories {
        google()
        mavenCentral()


    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath ("com.android.tools.build:gradle:$agp_version1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.firebase:perf-plugin:1.4.2")

    }

}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

