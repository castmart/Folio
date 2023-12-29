plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.2.1"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":details"))

    implementation(libs.spring.boot.mvc)
    implementation(libs.spring.boot.jdbc)
    implementation(libs.postgres.jdbc)

    implementation(libs.jakarta.servlet)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.jsr310)
    implementation(libs.jackson.module.kotlin) {
        because("It helps supporting the json encoding with kotlin data classes")
    }

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.spring.ext)
    testImplementation(libs.mockk)
    testImplementation(libs.spring.test)
    testImplementation(libs.kotest.testcontainers)
    testImplementation(libs.testcontainers.postgres)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
