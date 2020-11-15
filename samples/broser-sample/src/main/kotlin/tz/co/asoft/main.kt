package tz.co.asoft

fun main() {
    console.log("Works")
    val kfg = konfig()
    val json = Mapper.encodeToString(kfg)
    console.log(kfg)
    println("works: $json")
    val link: String by kfg
    println("It works: Link found was ${link.capitalize()}")
}