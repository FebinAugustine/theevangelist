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
//    implementation(libs.koin.androidx.viewmodel)

    // Project level dependencies
    implementation(project(":core:data"))
    implementation(project(":core:domain")) // <<< --- ENSURED THIS LINE IS PRESENT
    implementation(project(":features:auth:data"))
    implementation(project(":features:auth:domain"))
    implementation(project(":features:auth:ui"))


    // Ktor clien
    implementation(libs.ktor.client.core)

    // timber
    implementation(libs.timber)

}
