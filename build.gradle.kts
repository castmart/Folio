import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
}

subprojects {
    // Common configurations for all subprojects
    group = "com.castmart.folio.api" // Set common group ID
    version = "1.0.0" // Set common version

    repositories {
        // Add common repositories for all subprojects if needed
        mavenCentral()
    }
}
ext {

    val springBootDataJPA = "org.springframework.boot:spring-boot-starter-data-jpa"
    val springBootWeb = "org.springframework.boot:spring-boot-starter-web"
    val springBooTest = "org.springframework.boot:spring-boot-starter-test"
    val springBootDependencyManagementVersion = "3.2.0"
    val springBootVersion = "3.2.0"

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
//
//application {
//    mainClass.set("MainKt")
//}