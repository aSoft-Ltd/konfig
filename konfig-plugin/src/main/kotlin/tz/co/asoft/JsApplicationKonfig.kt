package tz.co.asoft

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

class JsApplicationKonfig(val project: Project, val konfig: Konfig) {
    init {
        with(project) {
            prepareWebpackConfigDir()
            createWebpackTasks(konfig)
            createRunTasks(konfig)
        }
    }

    private fun Project.prepareWebpackConfigDir() {
        if (tasks.findByName("prepareWebpackConfigDir") != null) return
        tasks.create<PrepareWebpackConfigDirTask>("prepareWebpackConfigDir")
    }

    private fun Project.createWebpackTasks(konfig: Konfig) = tasks.create<Copy>("webpack${konfig.name.capitalize()}") {
        group = "webpack"
        dependsOn(
                "prepareWebpackConfigDir",
                konfig.generateKonfigFileTaskName,
                "browserProductionWebpack"
        )
        from("build/distributions")
        into("build/websites/${konfig.name}")
        doLast {
            delete("build/distributions")
        }
    }

    private fun Project.createRunTasks(konfig: Konfig) = tasks.create("run${konfig.name.capitalize()}") {
        group = "run"
        dependsOn(
                "prepareWebpackConfigDir",
                konfig.generateKonfigFileTaskName
        )
        if (konfig.type == Konfig.Type.DEBUG) {
            finalizedBy("browserDevelopmentRun")
        } else {
            finalizedBy("browserProductionRun")
        }
    }
}