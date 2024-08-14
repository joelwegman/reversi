import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

// TODO: expand test coverage

class ReversiTest : FunSpec({
	test("don't skip turn") {
		BoardState().skipTurn.shouldBe(false)
	}

	test("skip turn") {
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
		).skipTurn.shouldBe(true)
	}

	test("moves remain") {
		BoardState().gameOver.shouldBe(false)
	}

	test("game over") {
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
