import org.w3c.dom.*
import org.w3c.dom.events.*
import kotlinx.browser.document

// initial state
var boardState = BoardState()

// move click event handler
@JsExport
fun moveTo(event: Event, element: HTMLElement) {
	event.preventDefault()
	event.stopPropagation()
	val x = (element.attributes.getNamedItem("data-x")?.value?:"0").toIntOrNull()?:0
	val y = (element.attributes.getNamedItem("data-y")?.value?:"0").toIntOrNull()?:0

	boardState = boardState.stateFromMove(x, y)
	render()
}

// skip turn event handler
@JsExport
fun skipTurn() {
	boardState = boardState.skipTurn()
	render()
}

// restart click handler
@JsExport
fun restart() {
	boardState = BoardState()
	render()
}

fun render() {
	document.getElementById("game")?.innerHTML = boardState.toHTML()
}

fun main() {
	render()
}
