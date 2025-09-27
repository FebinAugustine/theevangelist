package com.febin.buildlogic.helperconfigs

import com.android.build.api.dsl.CommonExtension

internal fun configureBuildTypesDefaults(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    //Configure build types
    commonExtension.run {
        buildTypes {
            // Debug build type (development)
            named("debug") {
                isMinifyEnabled = false
                enableUnitTestCoverage = true  // Fixed: For unit tests
                enableAndroidTestCoverage = true  // Fixed: For instrumented tests
                enableAndroidTestCoverage = true  // Enable coverage reports
            }

            // Release build type (production)
            named("release") {
                isMinifyEnabled = false  // Enable code shrinking
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"  // Your custom ProGuard rules
                )
                // Optional: Signing config (inject via signingConfig = signingConfigs.getByName("release"))
                // isDebuggable = false  // Implicitly false
            }
        }

    }
}
