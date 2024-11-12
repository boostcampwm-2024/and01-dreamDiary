plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.dreamdiary.android.library.compose)
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
}

dependencies {

    api(libs.material)
    api(libs.androidx.material3)
    api(libs.androidx.material.icons)
    api(libs.androidx.material.icons.android)

    implementation(libs.androidx.ui.text.google.fonts)
}
