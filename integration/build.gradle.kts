plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ksp)
}

dependencies {
  implementation(libs.kotlin.stdlibCommon)
  implementation(projects.core)
  implementation(projects.ksp)
  implementation(libs.mongo.driver)
  testImplementation(libs.bundles.kotest)
  ksp(projects.ksp)
}
