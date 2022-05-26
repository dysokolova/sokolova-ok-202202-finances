plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    // transport models
    implementation(project(":finances-common"))

    testImplementation(kotlin("test-junit"))
}
