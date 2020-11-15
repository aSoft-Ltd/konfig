plugins {
    id("tz.co.asoft.plugin.maker")
}

dependencies {
    api("com.android.tools.build:gradle:${versions.android.build_tools}")
}

gradlePlugin {
    plugins {
        val konfig by creating {
            id = "tz.co.asoft.konfig"
            implementationClass = "KonfigGradlePlugin"
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
    }
}

group = "tz.co.asoft"
version = "0.0.1"

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
    api("com.android.tools.build:gradle:${versions.android_build_tools}")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}")
    api("org.jetbrains.kotlin:kotlin-serialization:${versions.kotlin}")
}
