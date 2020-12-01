plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

repositories {
    google()
    jcenter()
}

android {
    configureAndroid("src/androidMain")
    defaultConfig {
        minSdk = 18
    }
}

group = "tz.co.asoft"
version = "2020.2"

kotlin {
    android {
        targetJava("1.8")
    }

    jvm {
        targetJava("1.8")
    }

    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("tz.co.asoft:konfig:0.0.2")
            }
        }
    }
}