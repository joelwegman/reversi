import org.w3c.dom.*
import org.w3c.dom.events.*

var boardState = BoardState(1, listOf(
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	listOf( 0,  0,  0,  1, -1,  0,  0,  0),
	listOf( 0,  0,  0, -1,  1,  0,  0,  0),
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	listOf( 0,  0,  0,  0,  0,  0,  0,  0),
))

// click event handler
@JsExport
fun moveTo(event: Event, element: HTMLElement): Unit {
	event.preventDefault()
	event.stopPropagation()
	val x = (element.attributes.getNamedItem("data-x")?.value?:"0").toIntOrNull()?:0
	val y = (element.attributes.getNamedItem("data-y")?.value?:"0").toIntOrNull()?:0

	var gameOver = false
	var skipTurn = false
	boardState = newState(boardState, x, y)
	if (!hasValidMoves(boardState)) {
		val toggledState = BoardState(boardState.turn * -1, boardState.grid.toList())
		if (!hasValidMoves(toggledState)) {
			gameOver = true
		}
		else {
			skipTurn = true
			boardState = toggledState
		}
	}
	render(boardState)

	// TODO: use a modal instead
	if (gameOver)
		js("alert('Game over.')")
	if (skipTurn) {
		js("alert('Player has no moves, skipping turn')")
	}
}

fun main() {
	render(boardState)
}
