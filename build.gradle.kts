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

group = "com.haysmike"
version = "0.1"
description = "MFTwister"
java.sourceCompatibility = JavaVersion.VERSION_1_8
