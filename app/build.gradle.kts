plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.example.moviesapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moviesapplication"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    android {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        kotlin {
            jvmToolchain(21)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.dagger)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.transport.api)
    implementation(libs.google.accompanist.navigation.material)
    kapt(libs.dagger.compiler)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    implementation(libs.androidx.material.icons.extended)
    kapt(libs.dagger.android.processor)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //leak canary
    debugImplementation(libs.leak.canary)

    implementation(project(":core:movies-api"))
    implementation(project(":features:movies-nowplaying"))
    implementation(project(":features:movies-details"))
    implementation(project(":features:movies-popular"))
    implementation(project(":features:movies-toprated"))
    implementation(project(":features:movies-upcoming"))
    implementation(project(":features:movies-actorfilms"))
    implementation(project(":features:movies-rating"))
    implementation(project(":features:movies-auth"))
    implementation(project(":core:core-di"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-navigation"))
}
