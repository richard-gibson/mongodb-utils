import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlinter) apply false
  alias(libs.plugins.detekt) apply false
}

allprojects {
  group = "com.github.mongo-kt-util"
  version = "0.1.0"

  repositories {
    mavenCentral()
  }
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "17"
    }
  }
  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "org.jmailen.kotlinter")
}


