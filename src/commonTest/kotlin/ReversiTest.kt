import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReversiTest : FunSpec({
	test("don't skip turn") {
		val state = BoardState()
		state.skipTurn.shouldBe(false)
	}

	test("skip turn") {
		val state = BoardState(
			grid = listOf(
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  1,  0,  0,  0),
				listOf( 0,  0,  0,  0, -1,  0,  0,  0),
				listOf( 0,  0,  0,  0,  1,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
				listOf( 0,  0,  0,  0,  0,  0,  0,  0),
			),
		)
		state.skipTurn.shouldBe(true)
	}
})
