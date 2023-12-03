package day3.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day3part2KtTest {

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
        assertEquals(467835, day3.part2.solve(input))
    }
}
