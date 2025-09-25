package com.febin.buildlogic.helperconfigs

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.gradle.kotlin.dsl.findByType

internal fun Project.configureKotlinAndroidDefaults(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        // Configure Kotlin options
        extensions.findByType(KotlinAndroidProjectExtension::class.java)
            ?.let { kotlinAndroidExt ->
                kotlinAndroidExt.jvmToolchain(17)
                kotlinAndroidExt.compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
    }
}
