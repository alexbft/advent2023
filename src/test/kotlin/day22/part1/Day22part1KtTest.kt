package day22.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day22part1KtTest {

    @Test
    fun solve() {
        val input = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent().lines()
        assertEquals(5, solve(input))
    }
}
