plugins {
    id("tz.co.asoft.gradle-plugin.lib-multiplatform")
    id("tz.co.asoft.gradle-plugin.lib-android")
}

kotlin {
    universalLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${versions.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-properties:${versions.serialization}")
            }
        }
    }
}
