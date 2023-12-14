package day14.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day14part1KtTest {

    @Test
    fun solve() {
        val input = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent().lines()
        assertEquals(136, solve(input))
    }
}
