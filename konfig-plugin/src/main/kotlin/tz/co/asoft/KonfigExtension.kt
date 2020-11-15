package tz.co.asoft

import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class KonfigExtension(val project: Project) {
    private var kommon = Konfig("common", mapOf())
    internal val konfigs = mutableListOf<Konfig>()

    @OptIn(ExperimentalStdlibApi::class)
    internal fun konfig(name: String, vararg properties: Pair<String, Any>) {
        if (konfigs.any { it.name.contains(name, ignoreCase = true) }) {
            error("Konfig $name already exists")
        }
        Konfig(name, buildMap {
            put("name", name)
            putAll(kommon.values)
            putAll(properties)
            val namespace = project.group.toString()
            put("namespace", namespace)
            put("package", "$namespace.$name")
            put("version", project.version.toString())
        }).also {
            konfigs.add(it)
            project.tasks.create<GenerateKonfigFileTask>("generate${name.capitalize()}KonfigFile") {
                konfig = it
            }
        }
    }

    fun common(vararg properties: Pair<String, Any>) {
        kommon = Konfig("common", kommon.values + properties)
    }

    fun debug(name: String, vararg properties: Pair<String, Any>) = konfig(name, *properties)
    fun debug(vararg properties: Pair<String, Any>) = debug("debug", *properties)
    fun staging(name: String, vararg properties: Pair<String, Any>) = konfig(name, *properties)
    fun staging(vararg properties: Pair<String, Any>) = staging("staging", *properties)
    fun release(name: String, vararg properties: Pair<String, Any>) = konfig(name, *properties)
    fun release(vararg properties: Pair<String, Any>) = release("release", *properties)
}