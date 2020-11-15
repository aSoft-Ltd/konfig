pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "konfig"

include(":konfig")
include(":konfig-plugin")

//includeBuild("../build-src")
//includeBuild("../frontend")
//
//include(":mapper")
//project(":mapper").projectDir = File("../mapper")
//
//include(":konfig-plugin")
//include(":konfig")
