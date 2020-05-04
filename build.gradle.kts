import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val jodaTimeVersion: String by project
val konfigVersion: String by project

group = "com.alumys.clean-architecture-sample_ktor"
version = "0.0.1-SNAPSHOT"

repositories {
    google()
    jcenter()
    mavenCentral()
}

plugins {
    java
    kotlin("jvm") apply false
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        mavenLocal()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
    }
    dependencies {
        implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
        implementation("ch.qos.logback", "logback-classic", logbackVersion)
        implementation("joda-time", "joda-time", jodaTimeVersion)
        implementation("org.koin", "koin-ktor", koinVersion)
        implementation("org.koin", "koin-logger-slf4j", koinVersion)
        implementation("com.natpryce", "konfig", konfigVersion)
        implementation(kotlin("script-runtime"))
    }
}
