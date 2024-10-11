plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.zzz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.aliyun.com/repository/public")
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
    implementation("com.azure:azure-storage-blob:12.26.1")
    implementation("com.squareup.retrofit2:retrofit:+")
    implementation("com.squareup.retrofit2:converter-gson:+")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}