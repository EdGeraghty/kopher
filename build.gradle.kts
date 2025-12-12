plugins {
    kotlin("jvm") version "2.2.21"
    id("org.sonarqube") version "7.2.1.6560"
    jacoco
}

group = "family.geraghty.ed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.1")
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

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}
