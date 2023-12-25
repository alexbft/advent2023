package day25.part1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day25part1KtTest {

    @Test
    fun solve() {
        val input = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
        """.trimIndent().lines()
        assertEquals(54, solve(input))
    }
}
