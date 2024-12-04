plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.medilert"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medilert"
        minSdk = 26
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Add Room dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.swiperefreshlayout)
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    
    // Add preference dependency if not already present
    implementation("androidx.preference:preference:1.2.1")
    
    // Add Google Maps dependency
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.11.0")
}