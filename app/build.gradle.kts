plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.example.androiddevelopercodechallenge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androiddevelopercodechallenge"
        minSdk = 24
        targetSdk = 35
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

            buildConfigField("int", "RESULTS", "20")
            buildConfigField("String", "SEED", "\"42c617e1aef82488\"")
            buildConfigField("String", "VERSION", "\"1.4\"")

        }
        debug {
            buildConfigField("int", "RESULTS", "20")
            buildConfigField("String", "SEED", "\"42c617e1aef82488\"")
            buildConfigField("String", "VERSION", "\"1.4\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hiltViewModel()
    implementation(libs.androidx.hilt.navigation.compose)
    //collectAsStateWithLifecycle()
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //NavHost()
    implementation(libs.androidx.navigation.compose)

    //coil to display images as url or compose
    implementation(libs.coil.compose)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    //Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Retrofit with Scalar Converter
    implementation(libs.converter.scalars)
    // Retrofit with Kotlin serialization Converter eza bde esta3mil serialization
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)

    //splashScreen
    implementation (libs.androidx.core.splashscreen)



    //Room database
    implementation (libs.androidx.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
    //converters for classes inside room database
    implementation (libs.gson)

    //paging with room
    implementation(libs.androidx.room.paging)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)



}
kapt {
    correctErrorTypes =  true
}