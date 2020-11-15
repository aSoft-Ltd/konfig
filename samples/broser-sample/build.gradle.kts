plugins {
    kotlin("js") version "1.4.10"
    id("tz.co.asoft.konfig")
    id("tz.co.asoft.application")
}

repositories {
    google()
    jcenter()
}

group = "tz.co.asoft"
version = "2020.2"

konfig {
    debug(
        "link" to "http://debug.com"
    )

    staging(
        "link" to "https://staging.com"
    )

    release(
        "link" to "https://release.com"
    )
}

kotlin {
    js(IR) {
        browser{
            webpackTask {
                devServer = devServer?.copy(
                    open = false
                )
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
}

dependencies {
    implementation("tz.co.asoft:konfig:0.0.1")
}