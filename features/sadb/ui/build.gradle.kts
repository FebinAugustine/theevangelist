plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.febin.features.sadb.ui"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    // Navigation Dependency
    implementation(libs.navigation.compose)

    // Koin
    implementation(libs.koin.androidx.compose)


    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Compose Dependencies
    implementation(libs.compose.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)

    // Material icons Dependency
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

}