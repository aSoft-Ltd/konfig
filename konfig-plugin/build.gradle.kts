plugins {
    id("asoft-gradle-plugin")
}

dependencies {
    api("com.android.tools.build:gradle:${versions.android.build_tools}")
    api(asoft("frontend"))
}

gradlePlugin {
    plugins {
        val konfig by creating {
            id = "tz.co.asoft.konfig"
            implementationClass = "KonfigGradlePlugin"
        }
    }
}