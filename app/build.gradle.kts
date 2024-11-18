import java.util.Properties

plugins {
    alias(libs.plugins.dreamdiary.android.application.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.gms.google.services)
}

android {
    signingConfigs {
        getByName("debug") {
            val keystoreProperties = Properties().apply {
                val file = rootProject.file("local.properties")
                if (file.exists()) {
                    load(file.inputStream())
                }
            }
            val keyStorePath = keystoreProperties.getProperty("KEYSTORE_PATH")
            storeFile = if (keyStorePath != null) {
                file(keyStorePath)
            } else {
                file("${System.getProperty("user.home")}/.android/debug.keystore")
            }
        }
    }
    namespace = "com.boostcamp.dreamteam.dreamdiary"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.feature.auth)
    implementation(projects.feature.diary)
    implementation(projects.feature.community)
    implementation(projects.feature.setting)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Timber
    implementation(libs.timber)
}
