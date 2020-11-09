pluginManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
rootProject.name = "konfig"
listOf("../../build-src/code").mapNotNull {
    file(it).takeIf { file -> file.exists() }
}.forEach {
    includeBuild(it.relativeTo(file(".")))
}

include(":konfig")

//includeBuild("../build-src")
//includeBuild("../frontend")
//
//include(":mapper")
//project(":mapper").projectDir = File("../mapper")
//
//include(":konfig-plugin")
//include(":konfig")
