import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.22"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":details"))

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.kotest)
    testImplementation(libs.spring.test)
    testImplementation(libs.postgres.jdbc)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.spring.ext)
    testImplementation(libs.kotest.testcontainers)
    testImplementation(libs.testcontainers.postgres)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
