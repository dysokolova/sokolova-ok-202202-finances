plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":finances-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api(kotlin("test-junit"))
            }
        }
    }
}
