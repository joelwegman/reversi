import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReversiTest : FunSpec({
    test("initial board has valid moves") {
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
        hasValidMoves(boardState).shouldBe(true)
    }
})
