package tz.co.asoft

import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap

fun konfig(): Map<String, Any> {
    val error = Throwable("Can't locate konfig.json file")
    val classLoader = ClassLoader.getSystemClassLoader()
    val stream = classLoader.getResourceAsStream("konfig.json") ?: throw error
    val p = Properties.decodeFromMap<>(mapOf())
    return stream.bufferedReader().readText().toJsonObject().toMap()
}