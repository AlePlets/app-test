pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ( url ="https://jitpack.io" ) // <-- Add this line

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url= "https://jitpack.io" ) // <-- Add this line

    }
}

rootProject.name = "lillup"
include(":app")
 