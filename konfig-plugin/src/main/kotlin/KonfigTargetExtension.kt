import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class KonfigTargetExtension(val target: KotlinTarget) {
    companion object {
        val konfigs = mutableMapOf<KotlinTarget, MutableList<Konfig>>()
    }

    fun creating(vararg values: Pair<String, Any>) = object : ReadOnlyProperty<Any?, Konfig> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Konfig {
            return Konfig(property.name, mutableMapOf<String, Any>().apply {
                put("name", property.name)
                putAll(values)
            })
        }
    }

    fun getting(vararg values: Pair<String, Any>) = object : ReadOnlyProperty<Any?, Konfig> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Konfig {
            val kfg = KonfigProjectBuilder.konfigs.find { it.name == property.name } ?: throw Exception("Konfig with name ${property.name} not found")
            return kfg.copy(values = (kfg.values + values).toMutableMap())
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
        val list = konfigs.getOrPut(target) { mutableListOf() }
        list.merge(kfgs.toList())
        list.forEach { kfg ->
            val generateJson = createKonfigJsonFileTasks(target, kfg)
            when (target) {
                is KotlinAndroidTarget -> target.createTasks(kfg, generateJson)
                is KotlinJvmTarget -> target.createTasks(kfg, generateJson)
                is KotlinJsTarget -> target.createTasks(kfg, generateJson)
            }
        }
    }
}