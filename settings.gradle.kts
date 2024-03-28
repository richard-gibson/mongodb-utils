pluginManagement {
  val kotlinVersion: String by settings
  val kspVersion: String by settings
  val kotestVersion: String by settings
  plugins {
    kotlin("jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version kspVersion
    id("io.kotest.multiplatform") version kotestVersion
  }
  repositories {
    gradlePluginPortal()
    google()
  }
}
rootProject.name = "Mongodb-kt-utils"

include(":core")
include(":ksp")
include(":integration")

