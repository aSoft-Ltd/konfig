package tz.co.asoft

import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateKonfigFileTask : DefaultTask() {
    companion object {
        const val DEFAULT_KONFIG_FILE_NAME = "tz.co.asoft.konfig.json"
        fun defaultFolderLocation(project: Project) = when {
            project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> "build/resources/main"
            else -> "build/resources/main"
        }.let { File(it).apply { mkdirs() } }

//        fun defaultOutputDir(target: KotlinTarget, konfig: Konfig) = when (target) {
//            is KotlinAndroidTarget -> target.project.resolveDir("build/intermediates/merged_assets/${konfig.name}/out")
//            is KotlinJvmTarget -> target.project.resolveDir("build/processedResources/${target.name}/main")
//            is KotlinJsTarget -> target.project.resolveDir("build/resources/main")
//            else -> target.project.resolveDir("build/konfig")
//        }
    }

    @Input
    var konfig = Konfig("default", mapOf("name" to "default"))

    @OutputDirectory
    var outputDir = defaultFolderLocation(project)

    @get:OutputFile
    val outputFile
        get() = File(outputDir, DEFAULT_KONFIG_FILE_NAME)

    override fun getGroup() = "konfig"

    @TaskAction
    fun generate() {
        outputFile.writeText(JsonBuilder(konfig.values + ("name" to konfig.name)).toPrettyString())
    }
}