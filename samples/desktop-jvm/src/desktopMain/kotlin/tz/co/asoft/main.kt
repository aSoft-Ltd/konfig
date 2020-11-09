package tz.co.asoft

fun main() {
    val kfg = konfig()
    println("works: $kfg")
    val port = kfg["port"]
    println("Port: $port, isInt: ${port is Int}, Port +1 : ${port.toString().toInt() + 1}")
}