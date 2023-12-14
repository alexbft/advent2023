package day14.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day14part2KtTest {

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
        assertEquals(64, solve(input))
    }
}
