package day21.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day21part2KtTest {

    private val input = """
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

    @Test
    fun test1() {
        assertEquals(16, solve(input, 6))
    }

    @Test
    fun test2() {
        assertEquals(50, solve(input, 10))
    }

    @Test
    fun test3() {
        assertEquals(1594, solve(input, 50))
    }

    @Test
    fun test4() {
        assertEquals(6536, solve(input, 100))
    }

    @Test
    fun test5() {
        assertEquals(167004, solve(input, 500))
    }

    @Test
    fun test6() {
        assertEquals(668697, solve(input, 1000))
    }

    @Test
    fun test7() {
        assertEquals(16733044, solve(input, 5000))
    }
}
