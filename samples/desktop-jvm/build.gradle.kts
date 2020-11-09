plugins {
    id("sample-mpp")
    id("tz.co.asoft.konfig")
    id("tz.co.asoft.frontend")
}

repositories {
    google()
    jcenter()
}

version = "2020.2"

kotlin {
    jvm {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        mainClassName = "tz.co.asoft.MainKt"
        konfig {
            val debug by creating(
                "port" to 8080,
                "test" to true
            )

            val release by creating(
                "port" to 80,
                "test" to false
            )
            deploy(debug, release)
        }

        docker {
            build {

            }
            run {

            }
        }
    }

    jvm("desktop") {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        mainClassName = "tz.co.asoft.MainKt"
        konfig {
            val debug by creating(
                "port" to 8080,
                "test" to true
            )

            val release by creating(
                "port" to 80,
                "test" to false
            )
            deploy(debug, release)
        }

        docker {
            build {

            }
            run {

            }
        }
    }

    js {
        useCommonJs()
        konfig {
            val main by creating(
                "databases" to mapOf(
                    "graph" to listOf("neo4j"),
                    "document" to listOf("firebase", "mongo"),
                    "flatfile" to listOf("aqua")
                )
            )
            deploy(main)
        }

        docker {
            build {}
            run {
                ports = mapOf(90 to 80)
            }
        }
    }

    sourceSets {
        val allJvm by creating {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(asoft("konfig"))
            }
        }

        val jvmMain by getting {
            dependsOn(allJvm)
        }

        val desktopMain by getting {
            dependsOn(allJvm)
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
    }
}

konfig {
    val debug by creating(
        "version" to version
    )

    val release by creating(
        "newval" to true
    )

    deploy(debug, release)
}

kotlinFrontend {
    webpack { }
}
