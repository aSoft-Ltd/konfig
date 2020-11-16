package tz.co.asoft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

open class ApplicationGradlePlugin : Plugin<Project> {
    private fun Project.applyJvmConfiguration() {
        plugins.apply("application")
        afterEvaluate {
            extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
                ApplicationKonfigJvm(project, it, null)
            }
        }
    }

    private fun Project.applyJsConfiguration() = afterEvaluate {
        extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
            ApplicationKonfigJs(project, it, null)
        }
    }

    private fun Project.applyAndroidConfiguration() = afterEvaluate {
        extensions.findByType<KonfigExtension>()?.konfigs?.forEach {
            ApplicationKonfigAndroid(project, it, null)
        }
    }

    private fun Project.applyMultiplatformConfiguration() = afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.targets?.forEach { target ->
            val konfigs = extensions.findByType<KonfigExtension>()?.konfigs ?: return@forEach
            when (target) {
                is KotlinAndroidTarget -> konfigs.forEach { konfig ->
                    ApplicationKonfigAndroid(project, konfig, target)
                }

                is KotlinJvmTarget -> konfigs.forEach { konfig ->
                    ApplicationKonfigJvm(project, konfig, target)
                }

                is KotlinJsTarget -> konfigs.forEach { konfig ->
                    ApplicationKonfigJs(project, konfig, target)
                }

                is KotlinJsIrTarget -> konfigs.forEach { konfig ->
                    ApplicationKonfigJs(project, konfig, target)
                }
            }
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
            plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") -> applyMultiplatformConfiguration()
        }
    }
}