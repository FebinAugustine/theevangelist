pluginManagement {

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }


}

rootProject.name = "The Evangelist"
includeBuild("buildlogic")

include(":app")
include(":core:ui")
include(":core:data")
include(":features:auth:ui")
include(":features:auth:data")
include(":features:auth:domain")
include(":features:userdashboard:ui")
include(":features:userdashboard:data")
include(":features:userdashboard:domain")
include(":features:sadb:ui")
include(":features:sadb:data")
include(":features:sadb:domain")
include(":di:data")
