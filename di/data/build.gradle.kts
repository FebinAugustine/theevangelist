plugins {
    id("convention.android.library")
}

android {
    namespace = "com.febin.di.data"
}

dependencies {

    // Koin dependency
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
}
    
