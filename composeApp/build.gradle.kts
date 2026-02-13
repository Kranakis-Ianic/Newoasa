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

    // Register Android target
    androidTarget {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0"
        summary = "Compose App"
        homepage = "https://github.com/Kranakis-Ianic/Newoasa"

        ios.deploymentTarget = "14.1"

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        // MapLibre Native iOS SDK
        pod("MapLibre") {
            version = "6.7.1"
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
            // Lifecycle - Android-specific
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            // Android-only UI libraries
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.androidx.datastore.preferences)
            // Ktorfit (Android/JVM-specific)
            implementation(libs.ktorfit.lib)
            
            // MapLibre Native Android SDK
            implementation(libs.maplibre.android)
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

            // Ktor - multiplatform
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin core (multiplatform)
            implementation(libs.koin.core)

            // Room (multiplatform/common artifacts) and bundled SQLite for native
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
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
}

dependencies {
    debugImplementation(libs.compose.uiTooling)

    // Room KSP - platform-specific and metadata (process annotations for commonMain)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspCommonMainMetadata", libs.room.compiler)

    // Ktorfit KSP - Platform specific only (Android)
    add("kspAndroid", libs.ktorfit.ksp)
}