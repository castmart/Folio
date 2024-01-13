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
    api(libs.project.reactor) {
        because("Should be provided in the configurations module through spring boot webflux")
    }
    api("jakarta.servlet:jakarta.servlet-api")
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
