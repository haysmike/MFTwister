plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.bitwig.com")
    }
}

dependencies {
    api(libs.bitwig.extension.api)
}

val bitwigExtensionsDir: String by project
tasks.register<Copy>("install") {
    from("build/libs") {
        include("mftwister-0.1.jar")
        rename {
            "MFTwister.bwextension"
        }
    }
    into(bitwigExtensionsDir)
    dependsOn(tasks.jar)
}

group = "com.haysmike"
version = "0.1"
description = "MFTwister"
java.sourceCompatibility = JavaVersion.VERSION_1_8
