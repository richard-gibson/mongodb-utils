val arrowVersion: String by project
val kotestVersion: String by project
val kspVersion: String by project
val kotestArrowVersion: String by project
val mongoDriverVersion: String by project
plugins {
  kotlin("jvm")
  id("com.google.devtools.ksp")
}

dependencies {

  implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
  implementation("io.arrow-kt:arrow-core:$arrowVersion")
  implementation("io.arrow-kt:arrow-optics:$arrowVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
  implementation("org.mongodb:mongodb-driver-core:$mongoDriverVersion")

  ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")
  testImplementation("io.kotest:kotest-property:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
  testImplementation("io.kotest.extensions:kotest-assertions-arrow:$kotestArrowVersion")
  testImplementation("io.kotest.extensions:kotest-property-arrow:$kotestArrowVersion") // optional
  testImplementation("io.kotest.extensions:kotest-property-arrow-optics:$kotestArrowVersion") // optional
  testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xallow-kotlin-package")
  }
}
