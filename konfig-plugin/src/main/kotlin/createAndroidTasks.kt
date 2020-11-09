import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

internal fun KotlinAndroidTarget.createTasks(kfg: Konfig, generateJsonFile: Task) {
    val fullName = name.capitalize() + kfg.name.capitalize()

    val androidExt = project.extensions.findByType(BaseAppModuleExtension::class.java)
    val variants = androidExt?.applicationVariants
    val variant = variants?.firstOrNull {it.name.equals(kfg.name,ignoreCase = true) } ?: return

    project.tasks.getByName("assemble${variant.name.capitalize()}").apply {
        group = "assemble"
        dependsOn(generateJsonFile)
    }

    val install = project.tasks.getByName("install${variant.name.capitalize()}").apply {
        group = "install"
        dependsOn(generateJsonFile)
    }

    project.tasks.create("installRun$fullName", Exec::class.java).apply {
        group = "run"
        dependsOn(generateJsonFile)
        dependsOn(install)
        commandLine("adb", "shell", "monkey", "-p", variant.applicationId + " 1")
        doLast { println("Launching ${variant.applicationId}") }
    }
}

