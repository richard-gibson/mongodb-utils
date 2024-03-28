plugins {
  kotlin("jvm")
}

allprojects {
  repositories {
    mavenCentral()
  }
  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
}

