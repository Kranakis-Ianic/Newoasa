// Safe global Java toolchain configuration applied from root build.gradle.kts
import org.gradle.jvm.toolchain.JavaLanguageVersion

subprojects {
    // Configure Java toolchain only if the Java plugin is present
    plugins.withType(org.gradle.api.plugins.JavaPlugin::class.java) {
        extensions.configure(org.gradle.api.plugins.JavaPluginExtension::class.java) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    // Also configure for Android projects (AGP exposes the java extension)
    plugins.withId("com.android.application") {
        extensions.configure(org.gradle.api.plugins.JavaPluginExtension::class.java) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
    plugins.withId("com.android.library") {
        extensions.configure(org.gradle.api.plugins.JavaPluginExtension::class.java) {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    // If a subproject applies Kotlin Multiplatform, configure its jvm toolchain to Java 17 as well
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java) {
            jvmToolchain(17)
        }
    }
}

