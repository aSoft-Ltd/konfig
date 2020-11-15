package tz.co.asoft

import java.io.Serializable

data class Konfig(val name: String, val values: Map<String, Any>): Serializable

val Konfig.generateKonfigFileTaskName get() = "generate${name.capitalize()}KonfigFile"