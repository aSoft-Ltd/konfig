package tz.co.asoft

class DockerRun {
    var ports = mapOf<Int, Int>()
    var volumes = mapOf<String, String>()
    var environment = mapOf<String, Any>()
}