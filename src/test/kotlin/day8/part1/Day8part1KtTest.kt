package day8.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day8part1KtTest {

    @Test
    fun solve() {
        val input = """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().lines()
        assertEquals(6, solve(input))
    }
}
