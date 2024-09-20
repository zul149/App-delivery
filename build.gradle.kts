// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val agp_version1 by extra("8.1.2")
    repositories {
        google()
        mavenCentral()


    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.1")
        classpath ("com.android.tools.build:gradle:$agp_version1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")

    }

}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

