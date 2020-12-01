plugins {
    id("com.android.library") version "4.1.0"
    kotlin("multiplatform") version "1.4.10"
    id("io.codearte.nexus-staging") version "0.22.0"
    id("tz.co.asoft.library") version "0.0.7"
    signing
}

object vers {
    object asoft {
        val konfig = "0.0.3"
        val mapper = "0.0.1"
    }
}

kotlin {
    universalLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("tz.co.asoft:kotlinx-serialization-mapper:${vers.asoft.mapper}")
            }
        }
    }
}

aSoftLibrary(
    version = vers.asoft.konfig,
    description = "A Library to help reading configurations"
)