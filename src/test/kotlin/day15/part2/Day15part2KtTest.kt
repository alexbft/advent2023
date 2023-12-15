package day15.part2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day15part2KtTest {

    @Test
    fun solve() {
        val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        assertEquals(145, solve(input))
    }
}
