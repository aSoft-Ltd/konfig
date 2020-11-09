import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import tz.co.asoft.DockerTargetExtension

fun KotlinTarget.konfig(builder: KonfigTargetExtension.() -> Unit) = KonfigTargetExtension(this).apply(builder)

fun KotlinJvmTarget.docker(builder: DockerTargetExtension.() -> Unit) = DockerTargetExtension(this).apply(builder)
fun KotlinJsTarget.docker(builder: DockerTargetExtension.() -> Unit) = DockerTargetExtension(this).apply(builder)