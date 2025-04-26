version = "0.1"

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

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks {
    val bitwigExtensionsDir: String by project
    register<Copy>("install") {
        from("build/libs") {
            include("mftwister-$version.jar")
            rename {
                "MFTwister.bwextension"
            }
        }
        into(bitwigExtensionsDir)
        dependsOn(jar)
    }
}
