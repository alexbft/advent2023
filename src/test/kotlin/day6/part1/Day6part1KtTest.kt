package day6.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day6part1KtTest {

    @Test
    fun solve() {
        val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().lines()
        assertEquals(288, solve(input))
    }
}
