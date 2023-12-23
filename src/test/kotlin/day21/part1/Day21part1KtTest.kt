package day21.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day21part1KtTest {

    @Test
    fun solve() {
        val input = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent().lines()
        assertEquals(16, solve(input, 6))
    }
}
