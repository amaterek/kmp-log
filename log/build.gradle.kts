plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

kotlin {
    jvm()
    js { browser {} }
    @Suppress("OPT_IN_USAGE")
    wasmJs { browser {} }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "util.log"
            isStatic = true
        }
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTarget.get()
            }
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockk.common)
        }
        jvmTest.dependencies {
            implementation(libs.junit)
            implementation(libs.mockk)
        }
    }

    android {
        publishLibraryVariants("release")
    }
}

android {
    namespace = "amaterek.util.log"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    dependencies {
        testImplementation(libs.junit)
        testImplementation(libs.mockk)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    repositories {
        maven {
            publications {
                register("release", MavenPublication::class) {
                    groupId = "com.github.amaterek"
                    artifactId = "kmp-log"
                    version = "0.4"
                }
            }
        }
    }
}