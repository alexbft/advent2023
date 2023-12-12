package day11.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day11part1KtTest {

    @Test
    fun solve() {
        val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent().lines()
        assertEquals(374, solve(input))
    }
}
