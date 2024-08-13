import kotlinx.browser.*
import org.w3c.dom.*
import org.w3c.dom.Element
import org.w3c.dom.events.*

val boardState = mapOf(
	"turn" to 1,
	"score" to mapOf("1" to 2, "-1" to 2),
	"grid" to listOf(
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  1, -1,  0,  0,  0),
		listOf( 0,  0,  0, -1,  1,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	)
)

fun isValid(state: Map<String, Any>, x: Int, y: Int): Boolean {
	return true
}

@JsExport
fun moveTo(event: Event, element: HTMLElement): Unit {
	event.preventDefault()
	event.stopPropagation()
	println("click")
}

fun render(state: Map<String, Any>) {
	val rendered =
		(state["grid"] as List<List<Int>>).mapIndexed { y, row ->
			"""<div class="row">""" +
			row.mapIndexed { x, item ->
				val playerNumber = when (item) {
					1 -> 0
					-1 -> 1
					else -> -1
				}
				val valid = isValid(state, x, y)
				val link = """<a onclick="reversi.moveTo(event, this)" class="move" href="?x=$x&y=$y#board" data-x="$x" data-y="$y">"""
				"""
					${if (valid) link else ""}
					<div class="square">
						<div class="piece p${playerNumber} ${if (item != 0) "pip" else ""}"></div>
					</div>
					${if (valid) "</a>" else ""}
				"""
			}.joinToString("") +
			"""</div>"""
		}.joinToString("")

	document.getElementById("board")?.innerHTML = rendered;
}

fun main() {
	render(boardState)
}
