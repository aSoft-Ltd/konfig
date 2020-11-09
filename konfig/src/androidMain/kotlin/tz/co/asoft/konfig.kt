package tz.co.asoft

import android.content.Context
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.properties.Properties

fun Context.konfig(): Map<String, Any> {
    val file = assets.open("konfig.json").bufferedReader()
    val json = file.readText()
    return Properties.encodeToMap()
}