package tz.co.asoft

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.io.File

open class PrepareWebpackConfigDirTask : DefaultTask() {
    @OutputDirectory
    var configDir: File = File(project.projectDir, "webpack.config.d").apply {
        if (!exists()) mkdirs()
    }

    @Internal
    var mppTarget: KotlinTarget? = null

    @Input
    var outputFilename: String = "konfig.js"

    @get:OutputFile
    val outputFile: File
        get() = File(configDir, outputFilename).apply {
            if (!exists()) createNewFile()
        }

    @TaskAction
    fun prepare() {
        outputFile.writeText("""config.resolve.modules.push("../resources/${mppTarget?.name ?: "main"}")""")
    }
}