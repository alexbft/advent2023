package day12.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day12part1KtTest {

    @Test
    fun solve() {
        val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent().lines()
        assertEquals(21, solve(input))
    }
}
