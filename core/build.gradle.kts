plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation(libs.mongo.driver)
}
kotlin {

  compilerOptions {
    freeCompilerArgs.add("-Xallow-kotlin-package")
  }
}
