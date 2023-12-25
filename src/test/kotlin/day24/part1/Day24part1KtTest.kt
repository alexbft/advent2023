package day24.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day24part1KtTest {

    @Test
    fun solve() {
        val input = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
        """.trimIndent().lines()
        assertEquals(2, solve(input, 7L..27L))
    }
}
