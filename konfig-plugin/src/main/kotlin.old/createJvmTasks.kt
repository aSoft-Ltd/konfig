import org.gradle.api.Task
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.Exec
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

private val mainClassNames = mutableMapOf<KotlinJvmTarget, String>()

var KotlinJvmTarget.mainClassName: String
    set(value) {
        mainClassNames[this] = value
    }
    get() = mainClassNames[this] ?: "undefined"

internal fun KotlinJvmTarget.createTasks(kfg: Konfig, generateJsonFile: Task) {
    val fullName = name.capitalize() + kfg.name.capitalize()

    val jar = project.tasks.findByName("${name}Jar") as? Jar ?: return

    val jarName = name + "-" + kfg.name

    val fatJar = project.tasks.create("fatJar$fullName", Jar::class.java) {
        it.group = "assemble"
        it.archiveBaseName.value(jarName)
        it.archiveVersion.value(project.version.toString())
        it.dependsOn(generateJsonFile)
        it.with(jar)
        it.doFirst { _ ->
            it.manifest { it.attributes["Main-Class"] = mainClassName }
            val compile = project.configurations.getByName("${name}RuntimeClasspath")
            val resDir = compilations.getByName("main").defaultSourceSet.resources
            it.from(resDir)
            it.from(compile.map { if (it.isDirectory) it else project.zipTree(it) })
        }
    }

    project.tasks.create("run$fullName", Exec::class.java) {
        it.group = "run"
        it.dependsOn(generateJsonFile)
        it.dependsOn(fatJar)
        it.workingDir("build/libs")
        it.commandLine("java", "-jar", "$jarName-${project.version}.jar")
    }
}

