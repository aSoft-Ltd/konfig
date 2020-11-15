package tz.co.asoft

import org.gradle.api.Project
import java.io.File

fun Project.resolveFile(path: String) = file(path).apply {
    if (!exists()) {
        parentFile?.mkdirs()
        createNewFile()
    }
}

fun Project.resolveDir(path: String) = file(path).apply {
    if (!exists()) mkdirs()
}

fun File.resolveDir(name: String) = File(this, name).apply {
    if (!exists()) mkdirs()
}

fun File.resolveFile(name: String) = File(this, name).apply {
    if (!exists()) {
        parentFile?.mkdirs()
        createNewFile()
    }
}

fun File.getOrCreateNewFile(name: String) = File(this, name).apply {
    if (!exists()) createNewFile()
}