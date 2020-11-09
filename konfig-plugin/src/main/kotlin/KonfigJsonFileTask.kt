import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import tz.co.asoft.getOrCreateNewFile
import tz.co.asoft.resolveDir
import java.io.File
import javax.inject.Inject

open class KonfigJsonFileTask @Inject constructor(
    @OutputDirectory var outputDir: File
) : DefaultTask() {

    companion object {
        fun defaultOutputDir(target: KotlinTarget, konfig: Konfig) = when (target) {
            is KotlinAndroidTarget -> target.project.resolveDir("build/intermediates/merged_assets/${konfig.name}/out")
            is KotlinJvmTarget -> target.project.resolveDir("build/processedResources/${target.name}/main")
            is KotlinJsTarget -> target.project.resolveDir("build/resources/main")
            else -> target.project.resolveDir("build/konfig")
        }
    }

    @Input
    var konfig: Konfig? = null

    @OutputFile
    val outputFile = outputDir.getOrCreateNewFile("konfig.json")

    @TaskAction
    fun doAction() {
        val k = konfig
        if (k != null) {
            outputFile.writeText(JsonBuilder(k.values + ("name" to k.name)).toPrettyString())
        }
    }
}