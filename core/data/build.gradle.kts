plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.febin.core.data"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.material3)
}
