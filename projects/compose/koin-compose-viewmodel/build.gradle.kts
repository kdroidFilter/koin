import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

val koinVersion: String by project
version = koinVersion

kotlin {
    jvmToolchain(1_8)
    jvm {
        withJava()
    }

    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    wasmJs {
        binaries.executable()
        nodejs()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":compose:koin-compose"))
            api(libs.compose.viewmodel)
            api(libs.compose.navigation)
        }
    }
}

rootProject.the<NodeJsRootExtension>().apply {
    nodeVersion = "21.0.0-v8-canary202309143a48826a08"
    nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
}

tasks.withType<KotlinCompile>().all {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

apply(from = file("../../gradle/publish.gradle.kts"))
