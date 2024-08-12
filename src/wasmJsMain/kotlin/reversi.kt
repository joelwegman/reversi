import kotlinx.browser.*
import kotlinx.dom.*

fun main() {
	document.body?.appendElement("div") {
		appendText("Hello, world!")
	}
}
