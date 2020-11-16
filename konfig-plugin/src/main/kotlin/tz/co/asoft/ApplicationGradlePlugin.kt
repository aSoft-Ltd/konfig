package tz.co.asoft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

open class ApplicationGradlePlugin : Plugin<Project> {
    private fun Project.applyJvmConfiguration() {
        plugins.apply("application")
        afterEvaluate {
            extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
                OnlyJvmApplicationKonfig(project, it)
            }
        }
    }

    private fun Project.applyJsConfiguration() = afterEvaluate {
        extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
            OnlyJsApplicationKonfig(project, it)
        }
    }

    private fun Project.applyAndroidConfiguration() = afterEvaluate {
        extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
            OnlyAndroidApplicationKonfig(project, it)
        }
    }

    override fun apply(project: Project) = with(project) {
        if (!plugins.hasPlugin("tz.co.asoft.konfig")) {
            plugins.apply(KonfigGradlePlugin::class.java)
        }
        when {
            plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> applyJvmConfiguration()
            plugins.hasPlugin("org.jetbrains.kotlin.js") -> applyJsConfiguration()
            plugins.hasPlugin("org.jetbrains.kotlin.android") -> applyAndroidConfiguration()
        }
    }
}