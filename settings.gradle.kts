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
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Movies Application"
include(":app")
include(":core:movies-api")
include(":features:movies-nowplaying")
include(":core:core-di")
include(":features:movies-details")
include(":core:core-ui")
include(":core:core-navigation")
include(":features:movies-actorfilms")
include(":features:movies-popular")
include(":core:core-model")
include(":features:movies-toprated")
include(":features:movies-upcoming")
include(":features:movies-rating")
include(":features:movies-auth")
