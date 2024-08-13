plugins {
    kotlin("multiplatform") version "2.0.10"
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser ()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting
    }
}