package day13.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day13part1KtTest {

    @Test
    fun solveBlock1() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent().lines()
        assertEquals(5, solveBlock(input))
    }

    @Test
    fun solveBlock2() {
        val input = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().lines()
        assertEquals(400, solveBlock(input))
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
        assertEquals(405, solve(input))
    }
}
