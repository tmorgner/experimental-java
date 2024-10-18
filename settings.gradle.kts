import experimental.build.plugin.AutoInclude

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

AutoInclude.getInstance().enabled = true
