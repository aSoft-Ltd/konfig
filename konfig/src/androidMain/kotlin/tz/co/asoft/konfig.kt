package tz.co.asoft

import android.content.Context

fun Context.konfig() = Mapper.decodeFromString(
    assets.open(KONFIG_JSON_FILE).bufferedReader().readText()
)