import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

// Semantic versioning from git tags (e.g. v1.2.3)
fun gitVersionName(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "describe", "--tags", "--match", "v*", "--abbrev=0")
            standardOutput = stdout
        }
        stdout.toString().trim().removePrefix("v")
    } catch (e: Exception) {
        "1.0.0"
    }
}

fun gitVersionCode(): Int {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-list", "--count", "HEAD")
            standardOutput = stdout
        }
        stdout.toString().trim().toInt()
    } catch (e: Exception) {
        1
    }
}

android {
    namespace = "dev.giuseppedarro.comanda"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dev.giuseppedarro.comanda"
        minSdk = 26
        targetSdk = 36
        versionCode = gitVersionCode()
        versionName = gitVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["mockk.agent.android.factory"] = "io.mockk.proxy.android.AndroidMockKAgentFactory"
        resourceConfigurations += listOf("en", "it", "nl")
    }

    androidResources {
        generateLocaleConfig = true
    }

    signingConfigs {
        create("release") {
            val keystoreFile = System.getenv("KEYSTORE_PATH")?.let { file(it) }
            if (keystoreFile != null && keystoreFile.exists()) {
                storeFile = keystoreFile
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        create("production") {
            dimension = "environment"
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val releaseSigningConfig = signingConfigs.getByName("release")
            if (releaseSigningConfig.storeFile != null) {
                signingConfig = releaseSigningConfig
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
            pickFirsts += "/META-INF/DEPENDENCIES"
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features:login"))
    implementation(project(":features:tables"))
    implementation(project(":features:orders"))
    implementation(project(":features:menu"))
    implementation(project(":features:printers"))
    implementation(project(":features:settings"))

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Koin
    implementation(libs.koin.android)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation("io.ktor:ktor-client-mock:2.3.12")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.truth)
    androidTestImplementation("io.mockk:mockk:1.13.5")
    androidTestImplementation("io.mockk:mockk-android:1.13.5")

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}