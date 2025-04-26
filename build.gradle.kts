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
        group = "build"
        description = "Installs Bitwig extension."
        dependsOn(jar)

        from("build/libs") {
            include("mftwister.jar")
            rename {
                "MFTwister.bwextension"
            }
        }
        into(bitwigExtensionsDir)
    }
}
