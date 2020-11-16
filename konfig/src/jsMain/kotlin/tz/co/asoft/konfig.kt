package tz.co.asoft

import kotlin.js.json

private external fun <T> require(module: String): T

fun konfig(): WrappedMap {
    val data = try {
        require<Map<String, Any>>(KONFIG_JSON_FILE)
    } catch (e: Throwable) {
        json()
    }
    return Mapper.decodeFromString(JSON.stringify(data))
}
