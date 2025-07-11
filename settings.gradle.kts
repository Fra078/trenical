
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // Aggiungi questa riga anche qui per le librerie di Compose
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "TrenicalProj"
include("proto")
include("common-backend")
include("common-frontend")
include("railway-service")
include("admin-console")
include("train-service")
include("user-service")
include("customer-client")
include("ticketry-service")
include("promotionService")
include("gateway-service")
include("payment-service")
include("gui-client")
