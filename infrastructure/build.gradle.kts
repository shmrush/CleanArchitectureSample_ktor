import java.util.*

val ktorVersion: String by project
val mysqlVersion: String by project
val flywayVersion: String by project
val exposedVersion: String by project
val commonsLang3Version: String by project

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

plugins {
    application
    id("org.flywaydb.flyway")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":usecase"))
    implementation(project(":interfaces"))

    implementation("io.ktor", "ktor-server-netty", ktorVersion)
    implementation("io.ktor", "ktor-server-core", ktorVersion)
    implementation("io.ktor", "ktor-auth", ktorVersion)
    implementation("io.ktor", "ktor-jackson", ktorVersion)
    implementation("io.ktor", "ktor-locations", ktorVersion)
    implementation("io.ktor", "ktor-server-sessions", ktorVersion)
    implementation( "mysql", "mysql-connector-java", mysqlVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.apache.commons", "commons-lang3", commonsLang3Version)

    testImplementation("io.ktor", "ktor-server-tests", ktorVersion)
}

flyway {
    url = applicationProperty("database.url")
    user = applicationProperty("database.user")
    password = applicationProperty("database.password")
}

fun applicationProperty(key: String): String {
    return Properties().run {
        load(project.rootProject.file("common/src/main/resources/application.properties").inputStream())
        getProperty(key)
    }
}
