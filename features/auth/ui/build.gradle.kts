plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.febin.features.auth.ui"

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.compose.runtime)

    // Navigation Dependency
    implementation(libs.navigation.compose)

}