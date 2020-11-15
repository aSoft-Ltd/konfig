package tz.co.asoft

import KonfigTargetExtension
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Exec
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import java.io.File

class DockerTargetExtension(private val target: KotlinTarget) {
    var dockerDir: File? = null
    var imageName: String? = null
    val project = target.project
    val konfigs = KonfigTargetExtension.konfigs.getOrPut(target) { mutableListOf() }

    private fun task(taskName: String): Task = project.tasks.findByName(taskName) ?: project.tasks.create(taskName)

    fun build(builder: DockerBuild.() -> Unit) {
        val dockerConfig = DockerBuild().apply(builder)
        konfigs.forEach { kfg ->
            val dir = dockerDir ?: project.file("build/docker/${target.name}/${kfg.name}")

            val dockerfile = dockerConfig.dockerFile
            val fullName = target.name.capitalize() + kfg.name.capitalize()

            val dockerClean = project.tasks.create("dockerClean$fullName", Delete::class.java) {
                it.group = "docker"
                it.delete = setOf(dir)
            }

            task("dockerClean").apply {
                group = "docker"
                dependsOn(dockerClean)
            }

            val dockerPrepare = project.tasks.create("dockerPrepare$fullName", Copy::class.java) {
                it.dependsOn(dockerClean)
                if (dockerfile == null) {
                    it.doFirst {
                        File(dir, "Dockerfile").apply {
                            dir.mkdirs()
                            createNewFile()
                            val text = when (target) {
                                is KotlinJvmTarget -> DockerBuild.defaultJarDockerfileContent()
                                is KotlinJsTarget -> DockerBuild.defaultWebDockerfileContent()
                                else -> ""
                            }
                            writeText(text)
                        }
                    }
                } else it.from(dockerfile)
                if (target is KotlinJvmTarget) {
                    it.from(project.tasks.getByName("fatJar$fullName"))
                } else if (target is KotlinJsTarget) {
                    it.dependsOn(project.tasks.getByName("deploy$fullName"))
                    it.from(project.file("build/deployment/${target.name}/${kfg.name}"))
                }
                it.rename { filename ->
                    if (filename.contains(".jar")) "app.jar" else filename
                }
                it.into(dir)
            }

            task("dockerPrepare").apply {
                group = "docker"
                dependsOn(dockerPrepare)
            }

            val buildArgs = dockerConfig.buildArgs ?: mapOf<String, Any>()

            val dockerBuild = project.tasks.create("dockerBuild$fullName", Exec::class.java) {
                it.group = "docker"
                it.description = "Build a docker image"
                it.workingDir = dir
                it.dependsOn(dockerPrepare)
                it.commandLine = listOf("docker", "build", ".") +
                    listOf("--file", dockerfile?.name ?: "Dockerfile") +
                    listOf("-t", imageName ?: "${target.name}-${kfg.name}:${project.version}") +
                    listOf("--build-arg", "JAR_FILE=app.jar") +
                    listOf("--no-cache", "--pull") +
                    buildArgs.flatMap { (k, v) -> listOf("--build-arg", "$k=\"$v\"") }
            }

            task("dockerBuild").apply {
                group = "docker"
                dependsOn(dockerBuild)
            }

            val dockerRemove = project.tasks.create("dockerRemove$fullName", Exec::class.java) {
                it.group = "docker"
                it.description = "Removes a docker image"
                it.workingDir = dir
                it.commandLine = listOf("docker", "image", "rm", imageName ?: "${target.name}-${kfg.name}:${project.version}")
            }

            task("dockerRemove").apply {
                group = "docker"
                dependsOn(dockerRemove)
            }
        }
    }

    fun run(builder: DockerRun.() -> Unit) {
        val runConfig = DockerRun().apply(builder)
        konfigs.forEach { kfg ->
            val dir = dockerDir ?: project.file("build/docker/${target.name}/${kfg.name}")
            val fullName = target.name.capitalize() + kfg.name.capitalize()
            val dockerRun = project.tasks.create("dockerRun$fullName", Exec::class.java) {
                it.group = "docker"
                it.description = "Runs an image in a tmp container"
                it.dependsOn("dockerBuild$fullName")
                it.workingDir = dir
                it.commandLine = listOf("docker", "run", "--rm") +
                    runConfig.ports.flatMap { (k, v) -> listOf("-p", "$k:$v") } +
                    runConfig.volumes.flatMap { (k, v) -> listOf("-v", "$k:$v") } +
                    runConfig.environment.flatMap { (k, v) -> listOf("-e", "$k=$v") } +
                    listOf(imageName ?: "${target.name}-${kfg.name}:${project.version}")
            }

            task("dockerRun").apply {
                group = "docker"
                dependsOn(dockerRun)
            }
        }
    }
}