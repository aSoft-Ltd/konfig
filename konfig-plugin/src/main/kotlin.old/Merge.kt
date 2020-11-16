fun Konfig.merged(other: Konfig): Konfig {
    if (name != other.name) throw Exception("Cannot merge konfig $name with ${other.name}")
    return Konfig(name = name, values = (values + other.values).toMutableMap())
}

fun Konfig.merge(other: Konfig) {
    val merged = merged(other)
    values.clear()
    values.putAll(merged.values)
}

fun List<Konfig>.merged(collection: Collection<Konfig>): List<Konfig> {
    val list = mutableListOf<Konfig>()
    val existingNames = map { it.name }
    val (mergees, nonMergees) = collection.partition { kfg -> existingNames.contains(kfg.name) }
    val untouched = this - mergees
    list.addAll(untouched)
    list.addAll(nonMergees)
    mergees.forEach { from ->
        val old = find { it.name == from.name } ?: throw Exception("Config ${from.name} doens't exist")
        list.add(old.merged(from))
    }
    return list
}

fun MutableList<Konfig>.merge(collection: Collection<Konfig>) {
    val list = merged(collection)
    clear()
    addAll(list)
}