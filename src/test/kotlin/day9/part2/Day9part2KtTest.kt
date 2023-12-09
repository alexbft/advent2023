package day9.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day9part2KtTest {

    @Test
    fun solve() {
        val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().lines()
        assertEquals(2, solve(input))
    }
}
