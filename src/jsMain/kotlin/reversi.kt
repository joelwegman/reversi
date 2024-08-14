import org.w3c.dom.*
import org.w3c.dom.events.*

// initial state
var boardState = BoardState()

// click event handler
@JsExport
fun moveTo(event: Event, element: HTMLElement) {
	event.preventDefault()
	event.stopPropagation()
	val x = (element.attributes.getNamedItem("data-x")?.value?:"0").toIntOrNull()?:0
	val y = (element.attributes.getNamedItem("data-y")?.value?:"0").toIntOrNull()?:0
	boardState = boardState.stateFromMove(x, y)
	boardState.render()

	// TODO: use a modal instead
	if (boardState.gameOver)
		js("alert('Game over.')")
	if (boardState.skipTurn) {
		js("alert('Player has no moves, skipping turn')")
	}
}

fun main() {
	boardState.render()
}
