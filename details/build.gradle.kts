import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.9.20"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":core"))
    implementation("org.springframework:spring-jdbc:6.0.7")
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
