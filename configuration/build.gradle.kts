plugins {
    kotlin("jvm") version "1.9.20"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":details"))

    // Spring boot mvc
    // Spring boot jdbc

    implementation(libs.jakarta.servlet)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.jsr310)
    implementation(libs.jackson.module.kotlin)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.spring.ext)
    testImplementation(libs.mockk)
    testImplementation(libs.spring.test)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
