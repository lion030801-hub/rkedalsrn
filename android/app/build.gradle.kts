plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "io.partsaver.prototype"
    compileSdk = 35
    defaultConfig {
        applicationId = "io.partsaver.prototype"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.1-web-prototype"
    }
    sourceSets["main"].assets.srcDir("../../dist")
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    buildTypes { getByName("release") { isMinifyEnabled = false } }
}
