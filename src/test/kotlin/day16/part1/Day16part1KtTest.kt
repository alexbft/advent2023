package day16.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day16part1KtTest {

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
        assertEquals(46, solve(input))
    }
}
