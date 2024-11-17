plugins {
    alias(libs.plugins.dreamdiary.android.feature)
    alias(libs.plugins.dreamdiary.android.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.community"

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
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coil
    implementation(libs.bundles.coil)

    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
}
