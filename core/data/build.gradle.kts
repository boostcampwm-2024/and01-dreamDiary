plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.boostcamp.dreamteam.dreamdiary.core.data"
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.model)
    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // firebase
    implementation(libs.firebase.firestore)
    implementation(libs.googleid)
    implementation("com.google.firebase:firebase-auth:21.1.0")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-auth-ktx")

    // Also add the dependency for the Google Play services library and specify its version
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
}
