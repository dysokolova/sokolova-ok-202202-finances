plugins {
    kotlin("multiplatform")
}


kotlin {
    jvm {}
//    linuxX64 {}

    sourceSets {
        val cache4kVersion: String by project
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":finances-common"))

                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")

            }
        }
        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":finances-repo-test"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
