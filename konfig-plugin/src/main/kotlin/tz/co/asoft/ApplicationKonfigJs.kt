package tz.co.asoft

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

class ApplicationKonfigJs(val project: Project, val konfig: Konfig, val mppTarget: KotlinTarget?) {
    init {
        with(project) {
            prepareWebpackConfigDir()
            createWebpackTasks()
            createRunTasks()
        }
    }

    private fun prepareTaskName() = "prepare${mppTarget?.name?.capitalize() ?: ""}WebpackConfigDir"

    private fun Project.prepareWebpackConfigDir() {
        if (tasks.findByName(prepareTaskName()) != null) return
        tasks.create<PrepareWebpackConfigDirTask>(prepareTaskName()).also {
            it.mppTarget = mppTarget
        }
    }

    private fun Project.createWebpackTasks() = tasks.create<Copy>("webpack${mppTarget?.name?.capitalize() ?: ""}${konfig.name.capitalize()}") {
        group = "webpack"
        val productionTaskName = (if (mppTarget != null) mppTarget.name + "B" else "b") + "rowserProductionWebpack"
        dependsOn(
            prepareTaskName(),
            konfig.generateKonfigFileTaskName(mppTarget),
            productionTaskName
        )
        from("build/distributions")
        if (mppTarget == null) {
            into("build/websites/${konfig.name}")
        } else {
            into("build/websites/${mppTarget.name}/${konfig.name}")
        }
        doLast {
            delete("build/distributions")
        }
    }

    private fun Project.createRunTasks() = tasks.create("run${mppTarget?.name?.capitalize() ?: ""}${konfig.name.capitalize()}") {
        group = "run"
        dependsOn(
            prepareTaskName(),
            konfig.generateKonfigFileTaskName(mppTarget)
        )
        val prefix = (if (mppTarget != null) mppTarget.name + "B" else "b") + "rowser"
        if (konfig.type == Konfig.Type.DEBUG) {
            finalizedBy("${prefix}DevelopmentRun")
        } else {
            finalizedBy("${prefix}ProductionRun")
        }
    }
}