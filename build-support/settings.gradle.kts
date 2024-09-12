@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
        create("testLibs") {
            from(files("../test-libs.versions.toml"))
        }
    }
}

include("build-plugin")
include("build-convention")
