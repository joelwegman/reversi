import kotlinx.browser.document

// convenience class for using .x and .y on points/vectors
data class Vec2 (var x: Int, var y: Int)

class BoardState (
	// which player's turn is it? 1 (white) or -1 (black)
	val turn: Int = 1,

	// this represents the board, where 0 is an empty square
	// NOTE: this is indexed as grid[y][x] rather than grid[x][y],
	// which means that values can be manually supplied to the
	// constructor in the same layout in source code as will be shown
	// in the ui and when converted to a string, e.g. for logging
	// or println debugging
	val grid: List<List<Int>> = listOf(
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  1, -1,  0,  0,  0),
		listOf( 0,  0,  0, -1,  1,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
		listOf( 0,  0,  0,  0,  0,  0,  0,  0),
	),
) {
	// the cardinal directions with arrows showing screen-space orientation
	private val directions: List<Vec2> = listOf(
		Vec2(0, -1),  // ↑
		Vec2(-1, -1), // ↖
		Vec2(-1, 0),  // ←
		Vec2(-1, 1),  // ↙
		Vec2(0, 1),   // ↓
		Vec2(1, 1),   // ↘
		Vec2(1, 0),   // →
		Vec2(1, -1)   // ↗
	)

	private val validMoves: List<List<Boolean>>
		get() =
			grid.mapIndexed { y, row ->
				row.mapIndexed { x, _ ->
					val updates = getUpdates(turn, x, y)
					updates.count() > 1
				}
			}

	// this represents a state where the new current player has no moves,
	// and requires special handling from the caller:
	// - render the board as-is
	// - indicate to the players what's happening
	// - skip that player's turn (submit a new state with turn *= -1)
	val skipTurn: Boolean
		get() = validMoves.flatten().filter { it }.isEmpty()

	// the game is over when neither player has any valid moves
	val gameOver: Boolean
		get() = skipTurn && BoardState(turn * -1, grid).skipTurn


	// create a new state from an old one and the player's move coordinates
	fun stateFromMove(x0: Int, y0: Int): BoardState {
		// get a set of all updates to make, rejecting the player's move if it
		// doesn't capture any of the opponent's pieces
		val allUpdates = getUpdates(turn, x0, y0)
		val updates = if (allUpdates.count() > 1) allUpdates else setOf()

		return BoardState(
			// toggle whose turn it is
			turn * -1,

			// merge the previous board with the updates
			grid.mapIndexed { y, row ->
				row.mapIndexed { x, it ->
					if (updates.contains(Vec2(x, y))) turn else it
				}
			},
		)
	}

	fun render() {
		// TODO: use a real templating engine instead of this mess
		val rendered =
			grid.mapIndexed { y, row ->
				"""<div class="row">""" +
					row.mapIndexed { x, item ->
						val valid = validMoves[y][x]
						val link = """<a onclick="reversi.moveTo(event, this)" class="move" href="?x=$x&y=$y#board" data-x="$x" data-y="$y">"""
						"""
							${if (valid) link else ""}
							<div class="square ${if (valid) "valid" else ""}">
								<div class="piece p${item} ${if (item != 0) "pip" else ""}"></div>
							</div>
							${if (valid) "</a>" else ""}
						"""
					}.joinToString("") +
				"""</div>"""
			}.joinToString("")

		document.getElementById("board")?.innerHTML = rendered
	}

	// get a list of board coordinates starting at a given point and continuing
	// along a given direction
	private fun getCoordsAlongRay(origin: Vec2, direction: Vec2): List<Vec2> {
		var count = 0
		val output: MutableList<Vec2> = mutableListOf()
		while (true) {
			val x = origin.x + count * direction.x
			val y = origin.y + count * direction.y
			if (x in 0..7 && y in 0..7)
				output.add(Vec2(x, y))
			else
				break
			count++
		}
		return output
	}

	// get all pieces to update from a given move
	private fun getUpdates(turn: Int, x: Int, y: Int): List<Vec2> {
		// don't allow moving to a space already occupied
		if (grid[y][x] != 0) return listOf()

		// collect coordinates in all directions, starting with where the player moved
		return directions.fold( listOf(Vec2(x, y)) ) { acc, dir ->
			// in the current direction, find the next coordinate of a piece belonging
			// to the current player
			val newOrigin = Vec2(x + dir.x, y + dir.y)
			val allCoords = getCoordsAlongRay(newOrigin, dir)
			val playerIndex = allCoords.indexOfFirst { grid[it.y][it.x] == turn }

			// the move is valid only if the player has a piece in that direction
			// and there are no empty squares before it
			val emptyIndex = allCoords.indexOfFirst { grid[it.y][it.x] == 0 }
			val valid = playerIndex > 0 && !(emptyIndex in 0 ..< playerIndex)

			// any coordinates left in that range are to be captured, so add them
			// to the accumulated list
			val capturedCoords = if (valid) allCoords.take(playerIndex) else listOf()
			acc + capturedCoords
		}.distinct()
	}
}

