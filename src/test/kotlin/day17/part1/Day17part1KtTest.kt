package day17.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day17part1KtTest {

    @Test
    fun solve() {
        val input = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent().lines()
        assertEquals(102, solve(input))
    }
}
