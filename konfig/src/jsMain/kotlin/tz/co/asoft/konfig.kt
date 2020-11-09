package tz.co.asoft

import kotlinx.serialization.json.JsonObject

private external fun <T> require(module: String): T

fun konfig() = try {
    require<Map<String, Any>>("konfig.json")
} catch (e: Throwable) {
    Unit
}.toJsonObject().toMap()

private fun <T> T.toJsonObject(): JsonObject = JSON.stringify(this).toJsonObject()