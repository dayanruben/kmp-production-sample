import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "RssReader"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            //CMP
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            //Compose Utils
            implementation(libs.coil.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            //Network
            implementation(libs.ktor.core)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.xml)
            //Coroutines
            implementation(libs.kotlinx.coroutines.core)
            //Logger
            implementation(libs.napier)
            //JSON
            implementation(libs.kotlinx.serialization.json)
            //Key-Value storage
            implementation(libs.multiplatform.settings)
            // DI
            api(libs.koin.core)
            implementation(libs.koin.compose)
            //Navigation
            implementation(libs.voyager.navigator)
            //Date formatting
            implementation(libs.kotlinx.datetime)
            implementation(libs.serialization)
            implementation(libs.core)
        }
        androidMain.dependencies {
            //Compose Utils
            implementation(libs.activity.compose)
            implementation(libs.accompanist.swiperefresh)
            //Coroutines
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.android)
            //DI
            implementation(libs.koin.core)
            implementation(libs.koin.android)

            implementation(libs.ktor.client.android)

            //WorkManager
            implementation(libs.work.runtime.ktx)
            //Splash
            implementation(libs.androidx.core.splashscreen)
        }
        iosMain.dependencies {
            //Network
            implementation(libs.ktor.client.ios)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)
        }
    }
}


android {
    namespace = "com.github.jetbrains.rssreader"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.jetbrains.rssreader"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.1"
    }

    signingConfigs {
        create("release") {
            storeFile = file("./key/key.jks")
            gradleLocalProperties(rootDir, providers).apply {
                storePassword = getProperty("storePwd")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPwd")
            }
        }
    }

    buildTypes {
        create("debugPG") {
            isDebuggable = false
            isMinifyEnabled = true
            versionNameSuffix = " debugPG"
            matchingFallbacks.add("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

