plugins {
    `java-library`
    kotlin("jvm")
    alias(libs.plugins.shadow)
    alias(libs.plugins.ktfmt.gradle)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.bitwig.com") }
}

dependencies {
    api(libs.bitwig.extension.api)
    implementation(kotlin("stdlib"))
}

tasks {
    shadowJar { dependencies { exclude(dependency(libs.bitwig.extension.api)) } }

    val bitwigExtensionsDir: String by project
    register<Copy>("install") {
        group = "build"
        description = "Installs Bitwig extension."
        dependsOn(shadowJar)

        from("build/libs") {
            include("mftwister-all.jar")
            rename { "MFTwister.bwextension" }
        }
        into(bitwigExtensionsDir)
    }
}

kotlin { jvmToolchain(21) }

ktfmt { kotlinLangStyle() }
