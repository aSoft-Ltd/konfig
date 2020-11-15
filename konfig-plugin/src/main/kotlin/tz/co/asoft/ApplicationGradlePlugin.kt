package tz.co.asoft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

open class ApplicationGradlePlugin : Plugin<Project> {
    private fun Project.applyJvmConfiguration() {
        plugins.apply("application")
        afterEvaluate {
            extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
                JvmApplicationKonfig(project, it)
            }
        }
    }

    private fun Project.applyJsConfiguration() {

    }

    override fun apply(project: Project) = with(project) {
        when {
            plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> applyJvmConfiguration()
            plugins.hasPlugin("org.jetbrains.kotlin.js") -> applyJsConfiguration()
        }
    }
}