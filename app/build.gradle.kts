plugins {
    alias(libs.plugins.androidAplication)
    alias(libs.plugins.jetbrainsKotlin)
    alias(libs.plugins.googleProtobuf)
    kotlin("plugin.serialization") version "1.9.21"

}

android {
    namespace = "no.uio.ifi.in2000.team_17"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.team_17"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled  = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.ycharts)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.sizeclass.android)

    //Lottie animation
    implementation(libs.lottie.compose)

    //noinspection GradleCompatible
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.material3.android)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.slf4j.simple)

    implementation  (libs.androidx.datastore)
    implementation  (libs.protobuf.javalite)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //LocalDateTime
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //SplashScreen
    implementation(libs.androidx.core.splashscreen)

}
protobuf{
    protoc{
        artifact = "com.google.protobuf:protoc:3.21.7"
    }
    plugins{
        generateProtoTasks{
            all().forEach {
                it.builtins {
                    create("java"){
                        option("lite")
                    }
                }
            }
        }
    }
}