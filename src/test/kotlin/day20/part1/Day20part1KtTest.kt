package day20.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day20part1KtTest {

    @Test
    fun solve() {
        val input = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
        """.trimIndent().lines()
        assertEquals(32000000L, solve(input, 1000))
    }

    @Test
    fun solve2() {
        val input = """
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
        """.trimIndent().lines()
        assertEquals(11687500L, solve(input, 1000))
    }
}
