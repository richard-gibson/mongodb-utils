import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
  group = "com.github.mongo-kt-util"
  version = "0.1-SNAPSHOT"

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
plugins {
  alias(libs.plugins.kotlin.jvm) apply false
}


