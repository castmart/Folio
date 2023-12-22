import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    // Common configurations for all subprojects
    group = "com.castmart.folio.api" // Set common group ID
    version = "1.0.0" // Set common version
    repositories {
        // Add common repositories for all subprojects if needed
        mavenCentral()
    }

    // Optionally configure plugin
//    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
//        debug.set(true)
//    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
