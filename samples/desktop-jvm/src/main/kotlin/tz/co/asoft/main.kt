package tz.co.asoft

fun main() {
    val kfg = konfig()
    val json = Mapper.encodeToString(kfg)
    println("works: $json")
    val link: String by kfg
    println("It works: Link found was $link")
}