package com.febin.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.febin.buildlogic.helperconfigs.configureBuildTypesDefaults
import com.febin.buildlogic.helperconfigs.configureKotlinAndroidDefaults
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply plugins
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")
             pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            // Get version catalog
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Android config
            extensions.configure<ApplicationExtension> {
                compileSdk = 36
                defaultConfig {
                    applicationId = "com.febin.theevangelist"
                    minSdk = 24
                    targetSdk = 36
                    versionCode = 1
                    versionName = "1.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                configureBuildTypesDefaults(this)
                buildFeatures {
                    compose = true
                }
                configureKotlinAndroidDefaults(this)
            }


            // Dependencies from catalog
            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("androidx-activity-compose").get())
                add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("implementation", libs.findLibrary("androidx-compose-ui").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                add("implementation", libs.findLibrary("androidx-compose-material3").get())

                // Tests
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
                add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
            }
        }
    }
}