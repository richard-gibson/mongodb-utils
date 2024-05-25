plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ksp)
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.kotlin.stdlibCommon)
  implementation(projects.core)
  implementation(libs.ksp)
  implementation(libs.autoservice)
  implementation(libs.bundles.kotlinPoet)
  implementation(libs.mongo.driver)
  ksp(libs.autoservice.ksp)
}
