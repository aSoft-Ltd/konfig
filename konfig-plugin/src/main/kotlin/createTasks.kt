import KonfigJsonFileTask.Companion.defaultOutputDir
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal fun createKonfigJsonFileTasks(target: KotlinTarget, kfg: Konfig) : KonfigJsonFileTask {
    val fullName = target.name.capitalize() + kfg.name.capitalize()
    val generateJsonFile = target.project.tasks.findByName("generateKonfigJsonFile$fullName") as? KonfigJsonFileTask
        ?: target.project.tasks.create("generateKonfigJsonFile$fullName", KonfigJsonFileTask::class.java, defaultOutputDir(target, kfg))
    generateJsonFile.apply {
        group = "konfig"
        description = "generates a json file from konfig maps"
        konfig = kfg
    }
    return generateJsonFile
}

