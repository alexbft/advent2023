package day1.day1part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day1part2KtTest {

    @Test
    fun solve() {
        val input = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent().lines()
        assertEquals(281, solve(input))
    }
}
