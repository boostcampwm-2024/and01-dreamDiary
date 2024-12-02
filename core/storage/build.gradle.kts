plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.storage"

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

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
