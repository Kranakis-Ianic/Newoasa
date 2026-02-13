import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    // Use Java 17 toolchain for Kotlin/JVM compilations
    jvmToolchain(17)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        compilations.getByName("main") {
            cinterops.all {
                // Make cinterop tasks depend on dependency resolution
                tasks.named(interopProcessingTaskName).configure {
                    dependsOn(":composeApp:transformCommonMainDependenciesMetadata")
                }
            }
        }
    }

    cocoapods {
        version = "1.0"
        summary = "Compose App"
        homepage = "https://github.com/Kranakis-Ianic/Newoasa"

        ios.deploymentTarget = "14.1"

        framework {
            baseName = "ComposeApp"
            isStatic = true

            // Export MapLibre and lifecycle dependencies
            // Do not export Android-only MapLibre Compose artifact here; iOS uses the `pod("MapLibre")` declaration below.

        }

        // Define the native dependency required by MapLibre Compose
        pod("MapLibre") {
            version = "6.17.1"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
            // Lifecycle - Android-specific (moved from commonMain to avoid K/N resolution on iOS)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            // MapLibre - Android artifact (keep out of commonMain)
            implementation(libs.maplibre.compose)
            // Android-only UI libraries
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.androidx.datastore.preferences)
            // Ktorfit (Android/JVM-specific) moved from commonMain
            implementation(libs.ktorfit.lib)
        }

        iosMain.dependencies {
            // Ktor client for iOS
            implementation(libs.ktor.client.darwin)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.materialIconsExtended)

            // MapLibre
            // NOTE: MapLibre Android artifact is added to androidMain. iOS uses the CocoaPods `MapLibre` pod.
            // MapLibre Compose has multiplatform artifacts; add to commonMain so iosMain can reference the `maplibre` package.
            implementation(libs.maplibre.compose)

            // Ktorfit
            // NOTE: Ktorfit is Android/JVM-specific and was moved to androidMain. Use multiplatform-safe HTTP clients in commonMain.
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin core (multiplatform)
            implementation(libs.koin.core)

            // Room (multiplatform/common artifacts) and bundled SQLite for native
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            // NOTE: Coil and Android DataStore are Android-only and moved to androidMain

            // NOTE: Lifecycle dependencies are Android-only and were moved to androidMain
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }

    // Pass compiler flags to suppress specific warnings
    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}

// Room configuration
room {
    schemaDirectory("$projectDir/schemas")
}

// KSP arguments
ksp {
    arg("room.generateKotlin", "true")
}

android {
    namespace = "com.example.newoasa"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.newoasa"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Configure Android Java toolchain so AGP uses Java 17 where supported
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)

    // Room KSP - platform-specific and metadata (process annotations for commonMain)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspCommonMainMetadata", libs.room.compiler)

    // Ktorfit KSP - Platform specific only
    add("kspAndroid", libs.ktorfit.ksp)
    // Note: do not register KSP for iOS for ktorfit to avoid loading its compiler plugin into Kotlin/Native.
}