plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "comp3350.wwsys"
    compileSdk = 35

    defaultConfig {
        applicationId = "comp3350.wwsys"
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
    buildFeatures {
        viewBinding = true
    }
}
repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
}
dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.espresso.contrib)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.espresso.core)
    testImplementation(libs.ext.junit)
    testImplementation(libs.android.espresso.core)
    testImplementation(libs.rules)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.hsqldb)
    implementation(kotlin("stdlib"))
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-inline:4.0.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    androidTestImplementation("com.android.support.test.espresso:espresso-accessibility:3.0.2")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test:rules:1.0.2")
}