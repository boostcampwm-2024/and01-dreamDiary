plugins {
    `kotlin-dsl`
}

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
        register("androidApplication") {
            id = "dreamdiary.android.application"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "dreamdiary.android.application.compose"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "dreamdiary.android.library"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "dreamdiary.android.library.compose"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "dreamdiary.android.feature"
            implementationClass = "com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.AndroidFeatureConventionPlugin"
        }
    }
}
