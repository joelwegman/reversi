import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReversiTest : FunSpec({
	test("initial board state has moves") {
		BoardState().hasMoves.shouldBe(true)
	}

	test("trivial no moves case") {
		BoardState(
			board = listOf(
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  1,  0,  0,  0),
				listOf( 0,  0,  0,  0, -1,  0,  0,  0),
				listOf( 0,  0,  0,  0,  1,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
			),
		).hasMoves.shouldBe(false)
	}

	test("simple no moves case") {
		BoardState(
			board = listOf(
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  1,  1,  1,  0,  0),
				listOf( 0,  0,  0,  1, -1,  1,  0,  0),
				listOf( 0,  0,  0,  1,  1,  1,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
			),
		).hasMoves.shouldBe(false)
	}

	test("initial state is not game over") {
		BoardState().gameOver.shouldBe(false)
	}

	test("simple game over case") {
		BoardState(
			board = listOf(
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  1,  1,  0,  0,  0),
				listOf( 0,  0,  0,  1,  1,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
			),
		).gameOver.shouldBe(true)
	}
})
