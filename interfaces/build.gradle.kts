val exposedVersion: String by project
val springSecurityCryptoVersion: String by project
val commonsLoggingVersion: String by project

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":usecase"))
    implementation(project(":domain"))

    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jodatime", exposedVersion)
    implementation("org.springframework.security", "spring-security-crypto", springSecurityCryptoVersion)
    implementation("commons-logging", "commons-logging", commonsLoggingVersion)
}