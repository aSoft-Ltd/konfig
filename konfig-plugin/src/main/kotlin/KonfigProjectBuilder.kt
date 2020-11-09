import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import tz.co.asoft.kotlinExt
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class KonfigProjectBuilder(val project: Project) {
    companion object {
        val konfigs = mutableListOf<Konfig>()
    }

    fun creating(vararg values: Pair<String, Any>) = object : ReadOnlyProperty<Any?, Konfig> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Konfig {
            return Konfig(property.name, mutableMapOf<String, Any>().apply {
                put("name", property.name)
                putAll(values)
            })
        }
    }

    fun creatingWith(with: Konfig, vararg values: Pair<String, Any>) = object : ReadOnlyProperty<Any?, Konfig> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Konfig {
            return Konfig(property.name, with.values.toMutableMap().apply {
                put("name", property.name)
                putAll(values)
            })
        }
    }

    fun deploy(vararg kfgs: Konfig) {
        project.kotlinExt?.targets?.filter {
            it is KotlinAndroidTarget || it is KotlinJvmTarget || it is KotlinJsTarget
        }?.forEach { target ->
            KonfigTargetExtension.konfigs.getOrPut(target) { mutableListOf() }.apply {
                merge(kfgs.toList())
                forEach { kfg -> createKonfigJsonFileTasks(target, kfg) }
            }
        }
        konfigs.addAll(kfgs)
    }
}