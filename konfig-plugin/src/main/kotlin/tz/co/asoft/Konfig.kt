package tz.co.asoft

import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.io.Serializable

data class Konfig(val name: String, val type: Type, val values: Map<String, Any>) : Serializable {
    enum class Type {
        DEBUG, STAGING, RELEASE
    }
}

fun Konfig.generateKonfigFileTaskName(mppTarget: KotlinTarget?): String {
    return if (mppTarget == null) {
        "generate${name.capitalize()}KonfigFile"
    } else {
        "generate${mppTarget.name.capitalize()}${name.capitalize()}KonfigFile"
    }
}