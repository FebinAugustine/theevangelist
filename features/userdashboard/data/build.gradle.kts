plugins {
    id("convention.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.febin.features.userdashboard.data"
}

dependencies {
    implementation(project(":features:userdashboard:domain"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Ktor dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)

    // Timber
    implementation(libs.timber)
}

