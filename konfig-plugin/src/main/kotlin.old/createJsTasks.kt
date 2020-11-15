import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.frontend.FrontendPlugin
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import tz.co.asoft.kotlinExt
import tz.co.asoft.resolveDir

internal fun KotlinJsTarget.createTasks(kfg: Konfig, generateJsonFile: Task) {
    if (!project.plugins.hasPlugin(FrontendPlugin::class.java)) {
        project.plugins.apply(FrontendPlugin::class.java)
    }

    if (!project.plugins.hasPlugin("kotlin-dce-js")) {
        project.plugins.apply("kotlin-dce-js")
    }

    val fullName = name.capitalize() + kfg.name.capitalize()

    project.tasks.create("run$fullName").apply {
        group = "run"
        dependsOn("stop")
        dependsOn(generateJsonFile)
        finalizedBy(project.tasks.getByName("run"))
    }

    val bundleTask = project.tasks.create("bundle$fullName").apply {
        group = "bundle"
        dependsOn(generateJsonFile)
        finalizedBy(project.tasks.getByName("bundle"))
    }

    project.tasks.create("deploy$fullName").apply {
        group = "deploy"
        dependsOn(bundleTask)
        doLast {
            val output = "build/deployment/${this@createTasks.name}/${kfg.name}"
            val deployDir = project.resolveDir(output)
            val mainSourceSets = project.kotlinExt?.sourceSets?.findByName("${this@createTasks.name}Main")
            val resources = mainSourceSets?.resources
            val resDirs = resources?.sourceDirectories?.filterNotNull() ?: listOf()
            project.copy {
                resDirs.forEach { dir -> it.from(dir) }
                it.from("build/bundle", "build/processedResources/js/main")
                it.into(deployDir)
            }
            println("deployed into ${deployDir.absolutePath}")
        }
    }
}

