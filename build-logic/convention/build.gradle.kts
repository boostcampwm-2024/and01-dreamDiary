plugins {
    `kotlin-dsl`
}

//group = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {

    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "dreamdiary.android.library"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "dreamdiary.android.feature"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidFeatureConventionPlugin"
        }
    }
}
