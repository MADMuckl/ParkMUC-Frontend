import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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
            baseName = "parkmuc"
            isStatic = true
        }
    }

//    js(IR) {
//        browser {
//            commonWebpackConfig {
//                outputFileName = "parkmuc.js"
//            }
//        }
//        binaries.executable()
//    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.maplibre.compose)

            val compassVersion = "1.0.0"
            implementation("dev.jordond.compass:geocoder:${compassVersion}")
            implementation("dev.jordond.compass:geocoder-mobile:${compassVersion}")
// Geolocation
            implementation("dev.jordond.compass:geolocation:${compassVersion}")
// To use geolocation you need to use one or more of the following
// Optional - Geolocation support for only iOS and Android
            implementation("dev.jordond.compass:geolocation-mobile:${compassVersion}")
// Optional - Location permissions for mobile (Android/iOS)
            implementation("dev.jordond.compass:permissions-mobile:${compassVersion}")
            implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "de.madmuc.parkmuc"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "de.madmuc.parkmuc"
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
    debugImplementation(compose.uiTooling)
}
