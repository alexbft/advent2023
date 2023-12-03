package day3.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day3part1KtTest {

    @Test
    fun solve() {
        val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...${'$'}.*....
            .664.598..
        """.trimIndent().lines()
        assertEquals(4361, day3.part1.solve(input))
    }
}
