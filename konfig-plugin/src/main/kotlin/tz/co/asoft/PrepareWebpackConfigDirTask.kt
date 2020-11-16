package tz.co.asoft

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PrepareWebpackConfigDirTask : DefaultTask() {
    @OutputDirectory
    var configDir: File = File(project.projectDir, "webpack.config.d").apply {
        if (!exists()) mkdirs()
    }

    @Input
    var outputFilename: String = "konfig.js"

    @get:OutputFile
    val outputFile: File
        get() = File(configDir, outputFilename).apply {
            if (!exists()) createNewFile()
        }

    @TaskAction
    fun prepare() {
        outputFile.writeText("""config.resolve.modules.push("../resources/main")""")
    }
}