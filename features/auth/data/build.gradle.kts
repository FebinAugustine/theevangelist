plugins {
    id("convention.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.febin.features.auth.data"

}

dependencies {

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.auth)

    implementation(libs.koin.core)

    // Timber
    implementation(libs.timber)

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":features:auth:domain"))
}




