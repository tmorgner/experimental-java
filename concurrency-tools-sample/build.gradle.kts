plugins {
    id("common-library")
}

dependencies {
    api(project(":concurrency-tools-api"))
    // ParallelMa does not yet have an equivalent in the API project
    implementation(project(":concurrency-tools-impl"))
}

