package tz.co.asoft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

open class KonfigGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val ext = extensions.create<KonfigExtension>("konfig", project)
        project.afterEvaluate {
            ext.konfigs.forEach { println("Configuring ${it.name}") }
        }
    }
}