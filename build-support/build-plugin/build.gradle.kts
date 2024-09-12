plugins {
    `java-gradle-plugin`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

gradlePlugin {
    // Define the plugin
    gradlePlugin {
        plugins {
            register("project-convention-plugin") {
                id = "project-convention"
                implementationClass = "experimental.build.plugin.ProjectConventionsPlugin"
            }
            register("project-settings-plugin") {
                id = "project-settings"
                implementationClass = "experimental.build.plugin.ProjectSettingsPlugin"
            }
        }
    }
}

repositories {
    gradlePluginPortal()
}
