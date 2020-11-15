plugins {
    kotlin("jvm") version "1.4.10"
    id("tz.co.asoft.konfig")
    id("tz.co.asoft.application")
}

repositories {
    google()
    jcenter()
}

group = "tz.co.asoft"
version = "2020.2"

application {
    mainClassName = "tz.co.asoft.MainKt"
}

konfig {
    common(
        "Main-Class" to "tz.co.asoft.MainKt"
    )

    debug(
        "link" to "http://debug.com",
        "Main-Class" to "tz.co.asoft"
    )

    staging(
        "link" to "https://staging.com"
    )

    release(
        "link" to "https://release.com"
    )
}

kotlin {
    target.compilations.all {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true
        }
    }
}

dependencies {
    implementation("tz.co.asoft:konfig:0.0.1")
}