plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.febin.core.ui"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    // Navigation Dependency
    implementation(libs.navigation.compose)
    implementation(libs.compose.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
}