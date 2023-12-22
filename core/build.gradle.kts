import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.mockk)
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