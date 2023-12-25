import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.9.20"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":core"))
    api(libs.spring.jdbc) {
        because("Should be provided in the configurations module through spring boot")
    }
    api(libs.spring.mvc) {
        because("Should be provided in the configurations module through spring boot")
    }
    api(libs.jakarta.servlet)
    api(libs.jackson.core)
    api(libs.jackson.annotations)
    api(libs.jackson.databind)
    api(libs.jackson.jsr310)
    api(libs.jackson.module.kotlin)
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.spring.ext)
    testImplementation(libs.mockk)
    testImplementation(libs.spring.test)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
