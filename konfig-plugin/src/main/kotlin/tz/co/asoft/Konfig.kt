package tz.co.asoft

import java.io.Serializable

data class Konfig(val name: String, val type: Type, val values: Map<String, Any>) : Serializable {
    enum class Type {
        DEBUG, STAGING, RELEASE
    }
}

val Konfig.generateKonfigFileTaskName get() = "generate${name.capitalize()}KonfigFile"