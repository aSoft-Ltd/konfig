package tz.co.asoft

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

val Project.kotlinExt get() = extensions.findByType(KotlinMultiplatformExtension::class.java)