plugins {
    kotlin("jvm") version "1.9.22"
    id("org.sonarqube") version "4.4.1.3373"
}

group = "family.geraghty.ed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
sonar {
    properties {
        property("sonar.projectKey", "EdGeraghty_kopher")
        property("sonar.organization", "edgeraghty")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
