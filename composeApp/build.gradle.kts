import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
            // This is crucial for distributing/linking
            export(libs.maplibre.compose)
        }

        // Define the native dependency required by MapLibre Compose
        pod("MapLibre") {
            version = "6.17.1"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    // Suppress "expect/actual classes are in Beta" warning
    sourceSets.all {
        languageSettings.optIn("kotlin.ExperimentalMultiplatform")
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
            implementation(compose.materialIconsExtended)

            implementation("org.maplibre.compose:maplibre-compose:0.11.1")
            implementation("org.maplibre.spatialk:geojson:0.6.1")
            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // DataStore
            implementation(libs.androidx.datastore.preferences)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    
    // Room KSP - platform-specific only (NOT commonMainMetadata)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    
    // Ktorfit KSP - Platform specific only
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
}
