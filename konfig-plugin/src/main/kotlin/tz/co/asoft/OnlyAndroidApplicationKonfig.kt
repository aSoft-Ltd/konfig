package tz.co.asoft

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

class OnlyAndroidApplicationKonfig(val project: Project, val konfig: Konfig) {
    init {
        with(project) {
            configureAssetsTasks(konfig)
            createInstallRunTasks(konfig)
        }
    }

    private fun Project.configureAssetsTasks(konfig: Konfig) {
        tasks.findByName(
            "merge${konfig.name.capitalize()}Resources"
        )?.apply {
            dependsOn(konfig.generateKonfigFileTaskName)
        }
    }

    private fun Project.createInstallRunTasks(konfig: Konfig) {
        val androidExt = project.extensions.findByType(BaseAppModuleExtension::class.java)
        val variants = androidExt?.applicationVariants
        val variant = variants?.find { it.name.equals(konfig.name, ignoreCase = true) } ?: return
        val installTask = tasks.findByName("install${konfig.name.capitalize()}") ?: return

        tasks.create<Exec>("installRun${konfig.name.capitalize()}") {
            group = "run"
            dependsOn(installTask)
            commandLine("adb", "shell", "monkey", "-p", variant.applicationId + " 1")
            doLast { println("Launching ${variant.applicationId}") }
        }
    }
}