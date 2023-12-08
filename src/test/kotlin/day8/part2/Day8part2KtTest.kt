package day8.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day8part2KtTest {

    @Test
    fun solve() {
        val input = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().lines()
        assertEquals(6, solve(input))
    }
}
