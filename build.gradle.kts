plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotest.multiplatform)
}

repositories {
	mavenCentral()
}

kotlin {
	targets {
		js {
			browser()
			binaries.executable()
		}
	}

	sourceSets {
		val commonMain by getting
		val commonTest by getting {
			dependencies {
				implementation(libs.kotest.assertions.core)
				implementation(libs.kotest.framework.engine)
				implementation(libs.kotest.framework.datatest)
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		val jsMain by getting
		val jsTest by getting
	}
}
