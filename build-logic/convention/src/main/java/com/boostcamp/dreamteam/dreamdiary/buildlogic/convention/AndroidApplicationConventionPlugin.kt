package com.boostcamp.dreamteam.dreamdiary.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.boostcamp.dreamteam.dreamdiary.buildlogic.convention.extension.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = "com.boostcamp.dreamteam.dreamdiary"
                    minSdk = 24
                    targetSdk = 35

                    versionCode = 1
                    versionName = "0.0.1"
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
