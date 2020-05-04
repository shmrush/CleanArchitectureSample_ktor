rootProject.name = "clean-architecture-sample_ktor"

include("common")
include("infrastructure")
include("domain")
include("usecase")
include("interfaces")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                gradle.rootProject.extra["kotlinVersion"]?.let { useVersion(it as String) }
            } else if (requested.id.id.startsWith("org.flywaydb.flyway")) {
                gradle.rootProject.extra["flywayVersion"]?.let { useVersion(it as String) }
            }
        }
    }
}
