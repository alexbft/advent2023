package day6.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day6part2KtTest {

    @Test
    fun solve() {
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().lines()
        assertEquals(71503, solve(input))
    }
}
