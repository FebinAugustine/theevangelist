plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.compose)
    // kotlinx serialization
    alias(libs.plugins.kotlin.serialization)
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

    // Koin
    implementation(libs.koin.androidx.compose)


    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // ktor client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.android)


    // Compose Dependencies
    implementation(libs.compose.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)

    // Material icons Dependency
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    // project specific dependencies
    implementation(project(":core:ui"))
    implementation(project(":features:auth:domain"))
    implementation(project(":core:domain"))

    // Timber
    implementation(libs.timber)
}


