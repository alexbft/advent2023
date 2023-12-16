package day16.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day16part2KtTest {

    @Test
    fun solve() {
        val input = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent().lines()
        assertEquals(51, solve(input))
    }
}
