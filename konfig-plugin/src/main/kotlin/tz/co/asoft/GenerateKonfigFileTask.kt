package tz.co.asoft

import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import java.io.File

open class GenerateKonfigFileTask : DefaultTask() {
    companion object {
        const val DEFAULT_KONFIG_FILE_NAME = "tz.co.asoft.konfig.json"
        fun defaultFolderLocation(project: Project, konfig: Konfig, mppTarget: KotlinTarget?) :File {
            val build = project.buildDir.absolutePath
            return when {
                project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> "$build/resources/main"
                project.plugins.hasPlugin("org.jetbrains.kotlin.js") -> "$build/resources/main"
                project.plugins.hasPlugin("org.jetbrains.kotlin.android") -> "$build/intermediates/merged_assets/${konfig.name}/out"
                project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") -> when (mppTarget) {
                    is KotlinAndroidTarget -> "$build/intermediates/merged_assets/${konfig.name}/out"
                    is KotlinJvmTarget -> "$build/processedResources/${mppTarget.name}/main"
                    is KotlinJsTarget -> "$build/resources/${mppTarget.name}"
                    is KotlinJsIrTarget -> "$build/resources/${mppTarget.name}"
                    else -> "$build/konfig/unsupported"
                }
                else -> "$build/konfig/unsupported"
            }.let { File(it).apply { mkdirs() } }
        }
    }

    init {
        group = "konfig"
    }

    @Internal
    var konfig = Konfig("default", Konfig.Type.DEBUG, mapOf("name" to "default"))

    @Internal
    var mppTarget: KotlinTarget? = null

    @get:OutputDirectory
    val outputDir: File
        get() = defaultFolderLocation(project, konfig, mppTarget)

    @get:OutputFile
    val outputFile
        get() = File(outputDir, DEFAULT_KONFIG_FILE_NAME)

    @TaskAction
    fun generate() {
        outputFile.writeText(JsonBuilder(konfig.values + ("name" to konfig.name)).toPrettyString())
    }
}