# Konfig
![badge][badge-maven] ![badge][badge-mpp] ![badge][badge-android] ![badge][badge-js] ![badge][badge-jvm]

A gradle tool that helps inject values during build configurations and that can be retrieve at runtime.

In other words, it brings buildTypes with structured basic data to kotlin-multiplatform applications

## Introduction
Ever wanted to have different build types in your multiplatform application? You feel like most of the time
your configurations during development time won't match those during deployment? `konfig` has you covered

## Samples
In your build logic (gradle), you can inject values like this
```kotlin
konfig {
    debug(
        "change" to 1,
        "link" to "http://debug.com"
    )

    staging(
        "link" to "https://staging.com"
    )

    release(
        "link" to "https://release.com"
    )
}
```
and you can retrieve those values in your application as simple as
```kotlin
val kfg = konfig()
val link: String by kfg // onDebug:(debug.com), onStaging(staging.com), onRelease(release.com)
```

## Setup:Gradle
Konfig comes with a gradle plugin as a well as a runtime library. Just do the following

### Kotlin Multiplatform
```kotlin
plugins {
    // . . . .
    id("tz.co.asoft.konfig") version "0.0.1"
}

// . . .

dependencies {
    implementation("tz.co.asoft:konfig:+") // please use the latest version possible
}
```

## Konfig Plugin
Setting up the konfig plugin makes it easy to help configure your buildTypes.

### DRY - Don't Repeat Yourself
Consider the following konfiguration
```kotlin
konfig{
    debug(
        "app_name" to "My Killer App",
        "api_link" to "https://debug.com"
    )
    . . .
    release(
        "app_name" to "My Killer App",
        "api_link" to "https://release.com"
    )
}
```
This repetition can be solved by the common konfiguration. Like so
```kotlin
konfig{
    common(
        "app_name" to "My Killer App"
    )
    debug(
        "api_link" to "https://debug.com"
    )
    . . .
    release(
        "api_link" to "https://release.com"
    )
}
```
Just remember, common is a reserved builder. Don't think it creates a buildType with the name "common"

### Nested Structures
konfig supports nested structures, and since it uses [kotlinx-serialization-mapper](https://github.com/aSoft-Ltd/kotlinx-serialization-mapper)
extracting your nested structure is easy
```kotlin
konfig {
    debug(
        "license" to "MIT",
        "devs" to listOf(
            mapOf(
                "name" to "John Doe",
                "email" to "john@doe.com"
            ),
            mapOf(
                "name" to "Jane Doe",
                "email" to "jane@doe.com"
            )
        )
    )
}
```
You can even go crazy and do something like
```kotlin
data class Dev(val name: String, val email: String)
konfig {
    debug(
        "license" to "MIT",
        "devs" to listOf(
            Dev(name = "John Doe", email = "john@doe.com") // or just Dev("John Doe","john@doe.com")
            Dev(name = "Jane Doe", email = "jave@doe.com"),
        )
    )
}
```

And you can retrieve your data as easy as
```kotlin
val kfg = konfig()
val license: String by kfg
val devs: List<Map<String,String>> by kfg

val name by devs[0]
val email by devs[1]

println(name) // John Doe
println(email) // jane@doe.com
```

### Naming your konfigs
You can name your konfigs if you wan't to, by default the names of the konfigs are as their buildType
i.e debug -> debug, staging->stagin, release->release
If you need to have a debug buildType named free, you can go just do as follows
```kotlin
konfig {
    debug("free_debug",
        "license" to "MIT"
    )

    debug("paid_debug"
        "licence" to "aSoft Ltd"
    )
}
```

## Application Plugin
Very often, you need to automate how you collect your configurations. This should be as easy as setting up your configurations and run
the build type you want. The `application` plugin gives you exactly that. But since every platform has it's own way of doing things we will tackle each of it seprately

### Android Application
#### Setup
To setup the android application, you must declare the gradle plugins as follows (the order is important)
```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
    id("tz.co.asoft.application")
}
// . . .
konfig{
    debug()
    staging()
    release()
}
```
#### Gradle Tasks
For each of the konfigurations above, the following gradle tasks are set
1. generate<Konfig>KonfigFile -> generates a json file with the set configuration
2. installRun<Konfig> -> a gradle task that installs and runs the specific version on a device or emulator

### JVM Application
This gradle plugin, applies the native java application plugin and ads a few tasks more

#### Setup
```kotlin
plugins {
    kotlin("jvm")
    id("tz.co.asoft.application")
}

application {
    mainClassName = "path.to.class.NameKt" // required by the `application` plugin
}
// . . .
konfig{
    debug()
    staging()
    release()
}
```
#### Gradle Tasks
1. generate<Konfig>KonfigFile -> generate a json file for the konfigurations set
2. fatJar<Konfig> -> generates a fat jar in the `build/libs` of the specific konfiguration
3. installDist<Konfig> -> generates a distributable in `build/binaries` which is package together with it dependencies
4. run<Konfig> -> runs the application depending on the buildType

### Browser Application
This shouldn't be confused with Javascript Applications. As we currently do not support nodejs applications
#### Setup
```kotlin
plugins {
    kotlin("js")
    id("tz.co.asoft.application")
}

// . . .
konfig{
    debug()
    staging()
    release()
}
```

#### Gradle Tasks
1. generate<Konfig>KonfigFile -> generates a json konfig file for that konfiguration
2. webpack<Konfig> -> creates a minified webpack bundle that can be deployed to any server
3. run<Konfig> -> opens the browser and runs the konfiguration. You should note that all debug types are not minified for obvious reasons, the rest are minified

### Multiplatform Application
This plugin helps you write multiplatform application in one code base
#### Setup
```kotlin
plugins {
    id("com.android.application") version "4.1.0"
    kotlin("multiplatform") version "1.4.10"
    id("tz.co.asoft.application")
}

// . . .
konfig{
    debug()
    staging()
    release()
}
```
#### Gradle Tasks
##### If you have an android target with the name "android", you will get the following tasks
1. generateAndroid<Konfig>KonfigFile -> generate the json config file for the android target
2. installRunAndroid<Konfig> -> install and runs the application on a phone or emulator

N.B: If your target has a different name, feel free to swap the name with the word androd on the tasks

##### If you have a jvm target with the name "jvm", you will get the following tasks
1. generateJVM<Konfig>KonfigFile -> generate the json config file for the android target
2. fatJarJvm<Konfig> -> generates a fat jar of the java app in the `build/libs` of the specific konfiguration
3. runJvm<Konfig> -> runs the jvm application

N.B: To run the JVM application from `kotlin("multiplatform")` gradle plugin, you must declare an attribute "Main-Class" in your Konfig

##### If you have a browser target with the name "js", you will get the following tasks
1. generateJs<Konfig>KonfigFile -> generates a json konfig file for that konfiguration
2. webpackJs<Konfig> -> creates a minified webpack bundle that can be deployed to any server
3. runJs<Konfig> -> opens the browser and runs the js app with the provided konfiguration. You should note that all debug types are not minified for obvious reasons, the rest are minified

[badge-maven]: https://img.shields.io/maven-central/v/tz.co.asoft/test/1.0.1?style=flat
[badge-mpp]: https://img.shields.io/badge/kotlin-multiplatform-blue?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-yellow.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/platform-jvm-orange.svg?style=flat
