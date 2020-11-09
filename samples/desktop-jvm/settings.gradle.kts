pluginManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

includeBuild("../../../build-src")
includeBuild("../../../frontend")
includeBuild("../../konfig-plugin")
includeBuild("../../konfig")
