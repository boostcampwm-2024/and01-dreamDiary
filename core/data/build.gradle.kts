plugins {
    alias(libs.plugins.dreamdiary.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.google.gms.google.services)
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
        schemaDirectory("$projectDir/schema")
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

    // paging
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.runtime)

    // firebase
    implementation(libs.firebase.firestore)
    implementation(libs.googleid)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
}
