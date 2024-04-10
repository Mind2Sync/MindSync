plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
}


android {
    namespace = "com.devdreamerx.mind_sync"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.devdreamerx.mind_sync"
        minSdk = 26
        targetSdk = 34
        renderscriptTargetApi = 34  // Adjust based on your project's API level
        renderscriptSupportModeEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    //Glide for image loading and blurred effects.
    //noinspection UseTomlInstead
    implementation("com.github.bumptech.glide:glide:4.13.2")
    //noinspection UseTomlInstead
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")

    //Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp3 logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //okHttp3
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")


    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    //EventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    //Base85
    implementation("commons-codec:commons-codec:1.15")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.0.6")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.androidx.navigation.fragment.ktx)
//    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0-alpha02")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0-alpha02")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}