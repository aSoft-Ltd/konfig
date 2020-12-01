plugins {
    kotlin("js")
    id("tz.co.asoft.application")
}

group = "tz.co.asoft"
version = "2020.2"

konfig {
    common(
        "Main-Class" to "tz.co.asoft.MainKt"
    )
    debug(
        "change" to 1,
        "authors" to mapOf(
            "architecture" to "andylamax@programmer.net"
        ),
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
        browser {
            commonWebpackConfig {
                outputFileName = "main.js"
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":todo-core"))
                implementation("tz.co.asoft:reakt-navigation:0.0.1")
            }
        }
    }
}