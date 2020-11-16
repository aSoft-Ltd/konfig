package tz.co.asoft

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import java.io.File

class ApplicationKonfigJvm(val project: Project, val konfig: Konfig, val mppTarget: KotlinJvmTarget?) {
    init {
        with(project) {
            if (mppTarget == null) {
                createFatJarTask()
                createInstallDistTask()
                createRunTask()
            } else {
                createMppFatJarTask(mppTarget)
                createMppRunTask(mppTarget)
            }
        }
    }

    private fun Project.createMppFatJarTask(target: KotlinJvmTarget) = tasks.create<Jar>("fatJar${target.name.capitalize()}${konfig.name.capitalize()}") {
        group = "assemble"
        archiveBaseName.value("${project.name}-${target.name}-${konfig.name}")
        archiveVersion.value(this@createMppFatJarTask.version.toString())
        dependsOn(konfig.generateKonfigFileTaskName(target))
        with(tasks.findByName("${target.name}Jar") as Jar)
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
            val runtime = configurations.getByName("${target.name}RuntimeClasspath")
            val resDir = target.compilations.getByName("main").defaultSourceSet.resources
            from(resDir)
            from(runtime.map { if (it.isDirectory) konfig else project.zipTree(it) })
        }
    }

    private fun Project.createMppRunTask(target: KotlinJvmTarget) = tasks.create<Exec>("run${target.name.capitalize()}${konfig.name.capitalize()}") {
        group = "run"
        dependsOn("fatJar${target.name.capitalize()}${konfig.name.capitalize()}")
        commandLine("java", "-jar", "${project.name}-${target.name}-${konfig.name}-${this@createMppRunTask.version}.jar")
        workingDir("build/libs")
    }

    private fun Project.createFatJarTask() = extensions.findByType<KotlinJvmProjectExtension>()?.apply {
        tasks.create<Jar>("fatJar${konfig.name.capitalize()}") {
            group = "assemble"
            archiveBaseName.value("${project.name}-${konfig.name}")
            archiveVersion.value(this@createFatJarTask.version.toString())
            dependsOn(konfig.generateKonfigFileTaskName(mppTarget))
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

    private fun Project.createInstallDistTask() = tasks.create<Copy>("installDist${konfig.name.capitalize()}") {
        group = "install"
        dependsOn(konfig.generateKonfigFileTaskName(mppTarget), "installDist")
        from(File(buildDir, "install/${project.name}"))
        into(File(buildDir, "binaries/${konfig.name}"))
        doLast {
            delete(File(buildDir, "install"))
        }
    }

    private fun Project.createRunTask() = tasks.create("run${konfig.name.capitalize()}") {
        group = "run"
        dependsOn(konfig.generateKonfigFileTaskName(mppTarget))
        finalizedBy("run")
    }
}