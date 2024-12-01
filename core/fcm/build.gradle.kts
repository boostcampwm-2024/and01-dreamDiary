plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.core.fcm"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // firebase messaging
    implementation(libs.firebase.messaging.ktx)
}
