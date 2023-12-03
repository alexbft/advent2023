package day1.day1part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day1part1KtTest {

    @Test
    fun solve() {
        val input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent().lines()
        assertEquals(142, solve(input))
    }
}
