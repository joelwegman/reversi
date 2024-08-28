import kotlinx.html.*
import kotlinx.html.stream.*

// convenience class for using .x and .y on points/vectors
data class Vec2 (var x: Int, var y: Int)

class BoardState (
	// which player's turn is it? 1 or -1
	private val turn: Int = 1,

	// this represents the game board, where 0 is an empty square
	// NOTE: this is indexed as board[y][x] rather than board[x][y],
	// so values can be manually supplied to the constructor in the same layout
	// in source code as will be shown in the ui and when converted to a string,
	// which also makes logging/debugging easier
	val board: List<List<Int>> = listOf(
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
	// does the current player have any moves available?
	val hasMoves: Boolean
		get() = validMoves.flatten().any { it }

	// the game is over when neither player has any valid moves
	val gameOver: Boolean
		get() = !hasMoves && !BoardState(turn * -1, board).hasMoves

	// skip this player's turn (for use when the player has no moves)
	fun skipTurn(): BoardState {
		return BoardState(turn * -1, board)
	}

	// create a new state from an old one and the player's move coordinates
	// TODO: make this a constructor?
	fun stateFromMove(x0: Int, y0: Int): BoardState {
		// get a set of all updates to make, rejecting the player's move if it
		// doesn't capture any of the opponent's pieces
		val allUpdates = getUpdates(turn, x0, y0)
		val updates = if (allUpdates.count() > 1) allUpdates else setOf()

		return BoardState(
			// toggle whose turn it is
			turn * -1,

			// merge the previous board with the updates
			board.mapIndexed { y, row ->
				row.mapIndexed { x, it ->
					if (updates.contains(Vec2(x, y))) turn else it
				}
			},
		)
	}

	// render the board into HTML
	fun toHTML(): String {
		return buildString { appendHTML().
			div {
				id = "score"
				div(classes = "player " + if (!gameOver && turn == 1) "active" else "") {
					div(classes = "pip p1")
					div(classes = "score") { + "${scores[0]}" }
				}
				div(classes = "player " + if (!gameOver && turn == -1) "active" else "") {
					div(classes = "pip p-1")
					div(classes = "score") { + "${scores[1]}" }
				}
			}

			appendHTML().div {
				id = "board"
				board.mapIndexed { y, row ->
					div(classes = "row") {
						row.mapIndexed { x, item ->
							val valid = validMoves[y][x]
							div(classes = "square ${item} " + if (valid) "valid" else "") {
								div(classes = "piece p${item} " + if (item != 0) "pip" else "") {
									if (valid)
										a(classes = "move", href = "?x=$x&y=$y#board") {
											attributes["data-x"] = "$x"
											attributes["data-y"] = "$y"
											onClick = "reversi.moveTo(event, this)"
										}
								}
							}
						}
					}
				}
			}

			appendHTML().div {
				id = "message"
				if (gameOver) {
					when {
						scores[0] > scores[1] -> {
							div(classes = "pip p1")
							div(classes = "text") { + "wins!" }
						}
						scores[1] > scores[0] -> {
							div(classes = "pip p-1")
							div(classes = "text") { + "wins!" }
						}
						else -> div(classes = "text") { + "Tied game!" }
					}
					a(classes = "restart") {
						onClick = "reversi.restart()"
						+ "Restart"
					}
				} else if (!hasMoves) {
					+ "You have no moves."
					a("?skip#board") {
						onClick = "reversi.skipTurn(event)"
						+ "Skip your turn."
					}
				} else {
					""
				}
			}
		}
	}

	////
	//// private members
	////

	// the cardinal directions, with arrows showing screen-space orientation
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

	// valid moves are those that would capture an opponent's piece, i.e. moving
	// moving there would update more than just the one piece the player moved
	private val validMoves: List<List<Boolean>>
		get() =
			board.mapIndexed { y, row ->
				row.mapIndexed { x, _ ->
					val updates = getUpdates(turn, x, y)
					updates.count() > 1
				}
			}

	private val scores: List<Int>
		get() = listOf(1, -1).map { player ->
			board.flatten().filter { it == player }.count()
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
		if (board[y][x] != 0) return listOf()

		// collect coordinates in all directions, starting with where the player moved
		return directions.fold( listOf(Vec2(x, y)) ) { acc, dir ->
			// in the current direction, find the next coordinate of a piece belonging
			// to the current player
			val newOrigin = Vec2(x + dir.x, y + dir.y)
			val allCoords = getCoordsAlongRay(newOrigin, dir)
			val playerIndex = allCoords.indexOfFirst { board[it.y][it.x] == turn }

			// the move is valid only if the player has a piece in that direction
			// and there are no empty squares before it
			val emptyIndex = allCoords.indexOfFirst { board[it.y][it.x] == 0 }
			val valid = playerIndex > 0 && !(emptyIndex in 0 ..< playerIndex)

			// any coordinates left in that range are to be captured, so add them
			// to the accumulated list
			val capturedCoords = if (valid) allCoords.take(playerIndex) else listOf()
			acc + capturedCoords
		}.distinct()
	}
}

