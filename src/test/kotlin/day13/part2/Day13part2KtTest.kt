package day13.part2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day13part2KtTest {
    @Test
    fun fixBlock1() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent().lines()
        assertEquals(300, fixBlock(input))
    }

    @Test
    fun fixBlock2() {
        val input = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().lines()
        assertEquals(100, fixBlock(input))
    }

    @Test
    fun solve() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().lines()
        assertEquals(400, solve(input))
    }
}
