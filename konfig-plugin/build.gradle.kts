plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.12.0"
}

repositories {
    mavenLocal()
    google()
    jcenter()
    mavenCentral()
}

gradlePlugin {
    plugins {
        val konfig by creating {
            id = "tz.co.asoft.konfig"
            implementationClass = "tz.co.asoft.KonfigGradlePlugin"
        }

        val application by creating {
            id = "tz.co.asoft.application"
            implementationClass = "tz.co.asoft.ApplicationGradlePlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/aSoft-Ltd/konfig"
    vcsUrl = website
    description = "A Library to inject configuration values to application"

    plugins {
        val konfig by getting {
            displayName = "Konfig Plugin"
            tags = listOf("kotlin", "configuration")
        }

        val application by getting {
            displayName = "Application Plugin"
            tags = listOf("kotlin", "application", "frontend")
        }
    }
}

object vers {
    val asoft_konfig = "0.0.1"
    val kotlin = "1.4.10"
}
group = "tz.co.asoft"
version = vers.asoft_konfig

defaultTasks("jar")

val sourcesJar by tasks.creating(org.gradle.jvm.tasks.Jar::class) {
    archiveClassifier.value("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(org.gradle.jvm.tasks.Jar::class) {
    archiveClassifier.value("javadoc")
}

artifacts {
    archives(sourcesJar)
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:${vers.kotlin}")
}
