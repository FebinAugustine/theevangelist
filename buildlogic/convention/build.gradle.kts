
plugins {
    `kotlin-dsl`
}



group = "com.febin.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    implementation(gradleApi())
}

gradlePlugin {
    plugins {

        create("androidApplicationConvention") {
            id = "convention.android.application"
            implementationClass = "com.febin.buildlogic.AndroidApplicationConventionPlugin"
        }
        create("androidLibraryConvention") {
            id = "convention.android.library"
            implementationClass = "com.febin.buildlogic.AndroidLibraryConventionPlugin"
        }
        create("kotlinLibraryConvention") {
            id = "convention.kotlin.library"
            implementationClass = "com.febin.buildlogic.KotlinLibraryConventionPlugin"
        }



    }
}