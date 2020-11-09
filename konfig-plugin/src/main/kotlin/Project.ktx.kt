import org.gradle.api.Project

fun Project.konfig(builder: KonfigProjectBuilder.() -> Unit) = KonfigProjectBuilder(this).apply(builder)