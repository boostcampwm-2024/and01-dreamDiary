plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.designsystem"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        compose = true
    }
}

dependencies {

    api(libs.material)
    api(libs.androidx.material3)
    api(libs.androidx.material.icons)
    api(libs.androidx.material.icons.android)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui.text.google.fonts)
}
