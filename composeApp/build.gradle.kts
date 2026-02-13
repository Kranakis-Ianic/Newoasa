import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "com.example.newoasa.ComposeApp")
        }
    }
    
    cocoapods {
        version = "1.0"
        summary = "OASA Transit App Shared Module"
        homepage = "https://github.com/Kranakis-Ianic/Newoasa"
        ios.deploymentTarget = "14.0"
        
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        
        // MapLibre Native iOS SDK
        pod("MapLibre") {
            version = "~> 6.0"
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
            // Android-only UI libraries
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.androidx.datastore.preferences)
            // Ktorfit (Android/JVM-specific)
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
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // MapLibre Compose
            implementation(libs.maplibre.compose)
            // SpatialK GeoJSON - MapLibre Compose 0.11.1 uses the old package
            implementation("io.github.dellisd.spatialk:geojson:0.2.0")
            
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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