
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

//     Add the Google services Gradle plugin
    id("com.google.gms.google-services")

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
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //    Lottie animation Dependency
    implementation("com.airbnb.android:lottie:6.6.2")

    //country code picker dependency
    implementation ("com.hbb20:ccp:2.7.3")

    //    GIF dependency
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")


//        implementation ("com.github.mukeshsolanki.android-otpview-pinview:otpview-compose:3.1.0")

        implementation ("com.github.mukeshsolanki.android-otpview-pinview:otpview:3.1.0")


        implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Skip this if you don't want to use integration libraries or configure Glide.
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    // Check the maven central badge for latest $version
    implementation ("com.github.pgreze:android-reactions:1.6")

    implementation ("com.github.3llomi:CircularStatusView:V1.0.3")

    implementation ("com.github.OMARIHAMZA:StoryView:1.0.2-alpha")

    implementation ("com.github.sharish:ShimmerRecyclerView:v1.3")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics:22.2.0")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth:23.2.0")

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")

    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")

    implementation("com.google.firebase:firebase-messaging:24.1.0")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

}
