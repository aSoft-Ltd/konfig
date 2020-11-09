package tz.co.asoft

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val KJson by lazy { Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)) }