

plugins {
    id("convention.kotlin.library")
}

dependencies {
    // project level dependencies
    implementation(project(":core:domain"))

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)



    




}