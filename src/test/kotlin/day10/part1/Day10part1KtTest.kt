package day10.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day10part1KtTest {

    @Test
    fun solve() {
        val input = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent().lines()
        assertEquals(8, solve(input))
    }
}
