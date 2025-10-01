plugins {
    id("convention.kotlin.library")
}

dependencies{
    // project level
    implementation(project(":core:domain"))

    // Coroutine dependencies
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

}