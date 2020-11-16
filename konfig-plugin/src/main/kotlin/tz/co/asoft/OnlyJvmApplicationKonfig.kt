package tz.co.asoft

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

class OnlyJvmApplicationKonfig(val project: Project, val konfig: Konfig) {
    init {
        with(project) {
            createFatJarTask(konfig)
            createInstallDistTask(konfig)
            createRunTask(konfig)
        }
    }

    private fun Project.createFatJarTask(konfig: Konfig) = extensions.findByType<KotlinJvmProjectExtension>()?.apply {
        tasks.create<Jar>("fatJar${konfig.name.capitalize()}") {
            group = "assemble"
            archiveBaseName.value("${project.name}-${konfig.name}")
            archiveVersion.value(version.toString())
            dependsOn(konfig.generateKonfigFileTaskName(null))
            with(tasks.findByName("jar") as Jar)
            doFirst {
                if (!konfig.values.containsKey("Main-Class"))
                    error("""
                        |Please add "Main-Class" attribute to ${konfig.name} configuration
                        |example
                        |konfig {
                        |    ${konfig.name}(
                        |        "Main-Class" to "tz.co.asoft.MainKt"
                        |    )
                        |}
                    """.trimMargin())
                manifest { attributes(konfig.values.mapValues { (_, v) -> v.toString() }) }
                val runtime = configurations.getByName("runtimeClasspath")
                val resDir = target.compilations.getByName("main").defaultSourceSet.resources
                from(resDir)
                from(runtime.map { if (it.isDirectory) konfig else project.zipTree(it) })
            }
        }
    }

    private fun Project.createInstallDistTask(konfig: Konfig) = tasks.create<Copy>("installDist${konfig.name.capitalize()}") {
        group = "install"
        dependsOn(konfig.generateKonfigFileTaskName(null), "installDist")
        from(File(buildDir, "install/${project.name}"))
        into(File(buildDir, "binaries/${konfig.name}"))
        doLast {
            delete(File(buildDir, "install"))
        }
    }

    private fun Project.createRunTask(konfig: Konfig) = tasks.create("run${konfig.name.capitalize()}") {
        group = "run"
        dependsOn(konfig.generateKonfigFileTaskName(null))
        finalizedBy("run")
    }
}