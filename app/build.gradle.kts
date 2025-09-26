plugins {
    id("convention.android.application")
}

android {
    namespace = "com.febin.theevangelist"

}

dependencies {

    // Koin Dependency
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    // Navigation Dependency
    implementation(libs.navigation.compose)

    // Coroutines Dependency
    implementation(libs.kotlinx.coroutines.android)

    // Timber
    implementation(libs.timber)

    // Project level dependency
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(project(":di:data"))

    implementation(project(":features:auth:data"))
    implementation(project(":features:auth:domain"))
    implementation(project(":features:auth:ui"))

    implementation(project(":features:sadb:data"))
    implementation(project(":features:sadb:domain"))
    implementation(project(":features:sadb:ui"))

    implementation(project(":features:userdashboard:data"))
    implementation(project(":features:userdashboard:domain"))
    implementation(project(":features:userdashboard:ui"))

}