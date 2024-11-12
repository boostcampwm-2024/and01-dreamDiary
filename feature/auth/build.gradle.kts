plugins {
    alias(libs.plugins.dreamdiary.android.feature)
    alias(libs.plugins.dreamdiary.android.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.feature.auth"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.data)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    androidTestImplementation(libs.androidx.espresso.core)

    // Core
    implementation(libs.androidx.activity.compose)

    // Coil
    implementation(libs.bundles.coil)

    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
}
