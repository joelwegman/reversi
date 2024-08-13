import kotlinx.browser.document

// The two players are represented internally as 1 and -1.
// 0 represents an empty square.
data class BoardState (val turn: Int, val grid: List<List<Int>>)

// create a new state from and old one and the player's move coordinates
fun newState(state: BoardState, x0: Int, y0: Int): BoardState {
    data class Vec2 (var x: Int, var y: Int)

    val directions = listOf(
        Vec2(0, -1),  // ↑
        Vec2(-1, -1), // ↖
        Vec2(-1, 0),  // ←
        Vec2(-1, 1),  // ↙
        Vec2(0, 1),   // ↓
        Vec2(1, 1),   // ↘
        Vec2(1, 0),   // →
        Vec2(1, -1))  // ↗

    // helper function to get a list of coordinates starting at a given point
    // and continuing along a given direction
    fun getCoordsAlongRay(origin: Vec2, direction: Vec2): List<Vec2> {
        var count = 0
        val coords: MutableList<Vec2> = mutableListOf()
        while (true) {
            val x = origin.x + count * direction.x
            val y = origin.y + count * direction.y
            if (x in 0..7 && y in 0..7)
                coords.add(Vec2(x, y))
            else
                break
            count++
        }
        return coords
    }

    // get the list of all coords to change, starting with the player's move
    val captures = directions.fold(setOf(Vec2(x0, y0))) { acc, dir ->
        // find the first coord with a piece belonging to the player
        val origin = Vec2(x0 + dir.x, y0 + dir.y)
        val allCoords = getCoordsAlongRay(origin, dir)
        val playerIndex = allCoords.indexOfFirst( { state.grid[it.y][it.x] == state.turn } )

        // if the move is invalid, don't capture anything, otherwise capture
        // everything between the player's move and their opposing piece
        val emptyIndex = allCoords.indexOfFirst({
            state.grid[it.y][it.x] == 0
        })
        val capturedCoords =
            if (playerIndex < 1 || (emptyIndex >= 0 && emptyIndex < playerIndex))
                listOf()
            else
                allCoords.take((playerIndex))
        acc union capturedCoords
    }.distinct()

    // return a new state with all captured pieces (including the player's move)
    // toggled to the player, and the turn toggled to the other player
    return BoardState(state.turn * -1, state.grid.mapIndexed { y, row ->
        row.mapIndexed { x, it ->
            if (captures.contains(Vec2(x, y))) state.turn else it
        }
    })
}

fun getValidMoves(state: BoardState): List<List<Boolean>> {
    // check whether a move is valid within a given state
    fun isValid(state: BoardState, x0: Int, y0: Int): Boolean {
        // coords already in use are invalid
        if (state.grid[y0][x0] != 0) return false

        // tentatively try the move and count how many pieces differ between it and
        // the current state; the move is valid if and only if the count is greater
        // than 1 (i.e. the move must capture some of the opponent's pieces)
        val potentialState = newState(state, x0, y0)
        var diffs = 0
        for (y in 0..<8) {
            for (x in 0..<8) {
                if (state.grid[y][x] != potentialState.grid[y][x]) diffs++
            }
        }
        return diffs > 1
    }

    return state.grid.mapIndexed { y, row ->
        List(row.size) { x -> isValid(state, x, y) }
    }
}

fun hasValidMoves(state: BoardState): Boolean {
    return getValidMoves(state).flatten().filter { it }.isNotEmpty()
}

fun render(state: BoardState) {
    // TODO: use a real templating engine instead of this mess
    val validMoves = getValidMoves(state)
    val rendered =
        state.grid.mapIndexed { y, row ->
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

    document.getElementById("board")?.innerHTML = rendered;
}
