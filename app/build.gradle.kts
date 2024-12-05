import java.util.Properties

plugins {
    alias(libs.plugins.dreamdiary.android.application.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
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
            val keyStorePath = keystoreProperties.getProperty("DEBUG_KEYSTORE_PATH")
            storeFile = if (keyStorePath != null) {
                file(keyStorePath)
            } else {
                file("${System.getProperty("user.home")}/.android/debug.keystore")
            }
        }

        create("release") {
            val keystoreProperties = Properties().apply {
                val file = rootProject.file("local.properties")
                if (file.exists()) {
                    load(file.inputStream())
                }
            }
            val keyStorePath = keystoreProperties.getProperty("RELEASE_KEYSTORE_PATH")
            val storePasswordValue = keystoreProperties.getProperty("RELEASE_KEYSTORE_PASSWORD")
            val keyAliasValue = keystoreProperties.getProperty("RELEASE_KEY_ALIAS")
            val keyPasswordValue = keystoreProperties.getProperty("RELEASE_KEY_PASSWORD")

            storeFile = file(keyStorePath)
            storePassword = storePasswordValue
            keyAlias = keyAliasValue
            keyPassword = keyPasswordValue
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
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.notification)
    implementation(projects.feature.auth)
    implementation(projects.feature.diary)
    implementation(projects.feature.community)
    implementation(projects.feature.setting)
    implementation(projects.feature.widget)
    implementation(projects.core.synchronization)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Timber
    implementation(libs.timber)
}
