plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":finances-transport-main-openapi"))
    implementation(project(":finances-common"))

    testImplementation(kotlin("test-junit"))
}
