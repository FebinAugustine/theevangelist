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
    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)
    // implementation(libs.ktor.client.timeout) // REMOVED
    // implementation(libs.ktor.client.retry)   // REMOVED
    implementation(libs.ktor.client.cio)

    // Timber
    implementation(libs.timber)

    // project level dependencies
    implementation(project(":core:domain"))


}
