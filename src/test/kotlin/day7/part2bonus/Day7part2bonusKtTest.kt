package day7.part2bonus

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day7part2bonusKtTest {

    @Test
    fun solve() {
        val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().lines()
        assertEquals(5905, solve(input))
    }
}
