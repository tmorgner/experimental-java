pluginManagement {
    includeBuild("build-support")
}

plugins {
    id("project-settings")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
        create("testLibs") {
            from(files("test-libs.versions.toml"))
        }
    }
}

rootProject.name = "experiments"

include("concurrency-tools-api")
include("concurrency-tools-impl")
include("concurrency-tools-sample")
