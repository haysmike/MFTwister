plugins {
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.ktfmt.gradle)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.bitwig.com") }
}

dependencies {
    api(libs.bitwig.extension.api)
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    val bitwigExtensionsDir: String by project
    register<Copy>("install") {
        group = "build"
        description = "Installs Bitwig extension."
        dependsOn(jar)

        from("build/libs") {
            include("mftwister.jar")
            rename { "MFTwister.bwextension" }
        }
        into(bitwigExtensionsDir)
    }
}

kotlin { jvmToolchain(21) }

ktfmt { kotlinLangStyle() }
