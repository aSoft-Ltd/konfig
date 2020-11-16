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
        fun defaultFolderLocation(project: Project, konfig: Konfig) = when {
            project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> "build/resources/main"
            project.plugins.hasPlugin("org.jetbrains.kotlin.js") -> "build/resources/main"
            project.plugins.hasPlugin("org.jetbrains.kotlin.android") -> "build/intermediates/merged_assets/${konfig.name}/out"
            else -> "build/resources/main"
        }.let { File(it).apply { mkdirs() } }
    }

    @Input
    var konfig = Konfig("default", Konfig.Type.DEBUG, mapOf("name" to "default"))

    @get:OutputDirectory
    val outputDir: File
        get() = defaultFolderLocation(project, konfig)

    @get:OutputFile
    val outputFile
        get() = File(outputDir, DEFAULT_KONFIG_FILE_NAME)

    override fun getGroup() = "konfig"

    @TaskAction
    fun generate() {
        outputFile.writeText(JsonBuilder(konfig.values + ("name" to konfig.name)).toPrettyString())
    }
}