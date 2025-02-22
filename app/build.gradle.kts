plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.employeecahtkt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.employeecahtkt"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //    Lottie animation Dependency
    implementation("com.airbnb.android:lottie:4.0.0")

    //country code picker dependency
    implementation ("com.hbb20:ccp:2.6.0")


//        implementation ("com.github.mukeshsolanki.android-otpview-pinview:otpview-compose:3.1.0")

        implementation ("com.github.mukeshsolanki.android-otpview-pinview:otpview:3.1.0")


        implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.bumptech.glide:glide:4.14.2")
    // Skip this if you don't want to use integration libraries or configure Glide.
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

}