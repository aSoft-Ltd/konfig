package tz.co.asoft

import kotlinx.browser.document

fun main() {
    console.log("Works")
    val konfig = konfig()
    val pckg = konfig["package"]
    document.getElementById("root")?.outerHTML = pckg.toString()
}