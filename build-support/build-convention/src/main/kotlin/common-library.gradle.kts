@file:Suppress("UnstableApiUsage")

import experimental.build.plugin.ProjectConventionsExtension

plugins {
    id("java")
    id("java-library")
    id("project-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    testCompileOnly("org.jetbrains:annotations:24.0.0")
    testCompileOnly("com.google.code.findbugs:jsr305:3.0.2")
    testFixturesCompileOnly("org.jetbrains:annotations:24.0.0")
    testFixturesCompileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

fun testLibs(name: String): ExternalModuleDependencyBundle? {
    val v = project.extensions.findByType(VersionCatalogsExtension::class) ?: return null
    val versionCatalog = v.find("testLibs").get()
    return versionCatalog.findBundle(name).get().get()
}

fun applyTestLibs(name: String, apply: (dependency: Any) -> Unit) : Unit {
    val lib = testLibs(name)
    lib?.forEach(apply)
}

tasks.test {
    val javaVersion = ProjectConventionsExtension.of(project).testVersion.get()
    if (javaVersion.canCompileOrRun(8)) {
        useJUnitPlatform()
        project.dependencies {
            applyTestLibs("junit5") { x -> testImplementation(x) }
        }
    }
}



