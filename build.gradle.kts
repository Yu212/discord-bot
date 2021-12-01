import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

group = "com.yu212"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:latest.release")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
