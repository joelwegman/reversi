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
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-js"))
				implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.11.0")
			}
		}

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
