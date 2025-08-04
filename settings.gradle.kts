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

rootProject.name = "Movies Application"
include(":app")
include(":core:movies-api")
include(":features:movies-nowplaying")
include(":core:state")
include(":core:core-di")
include(":features:movies-details")
include(":core:core-ui")
include(":core:core-navigation")
include(":features:movies-actorfilms")
