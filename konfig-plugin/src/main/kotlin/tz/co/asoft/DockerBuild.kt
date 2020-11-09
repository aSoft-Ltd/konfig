package tz.co.asoft

import Konfig
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import java.io.File

class DockerBuild {
    var dockerFile: File? = null
    var buildArgs: MutableMap<String, Any>? = null

    companion object {
        fun defaultJarDockerfileContent(): String = """
        FROM openjdk:8-jre-alpine
        ARG JAR_FILE
        COPY ${"$" + "{JAR_FILE}"} app.jar
        EXPOSE 8080
        ENTRYPOINT ["java","-jar","/app.jar"]
    """.trimIndent()

        fun defaultWebDockerfileContent(): String = """
        FROM httpd:2.4.46
        COPY . /usr/local/apache2/htdocs/
    """.trimIndent()
    }
}