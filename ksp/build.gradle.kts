val arrowVersion: String by project
val kotestVersion: String by project
val kspVersion: String by project
val kPoetKspVersion: String by project
val mongoDriverVersion: String by project

plugins {
  kotlin("jvm")
  id("io.kotest.multiplatform")
  id("com.google.devtools.ksp")
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

tasks.withType<Test> {
  useJUnitPlatform()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
  implementation("com.google.auto.service:auto-service-annotations:1.1.0")
  implementation("com.squareup:kotlinpoet-ksp:$kPoetKspVersion")
  implementation(project(":core"))
  ksp("dev.zacsweers.autoservice:auto-service-ksp:0.5.2")
  implementation("org.mongodb:mongodb-driver-core:$mongoDriverVersion")

}
