

plugins {
    id("convention.kotlin.library")
}

dependencies {
    // project level dependencies
    implementation(project(":core:domain"))
    // implementation(project(":features:auth:domain")) // REMOVED SELF-DEPENDENCY


}