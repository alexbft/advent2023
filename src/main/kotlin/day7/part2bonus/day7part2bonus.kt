package day7.part2bonus

import bootstrap.readAllLinesFromInput

private fun compareLists(a: List<Int>, b: List<Int>): Int {
    for ((aVal, bVal) in a.zip(b)) {
        if (aVal != bVal) {
            return aVal compareTo bVal
        }
    }
    return 0
}

private data class Hand(val cards: String, val bid: Int) {
    val rank = getMaxRankRec(cards, "")

    private fun getMaxRankRec(buffer: String, variant: String): List<Int> {
        if (buffer.isEmpty()) {
            return variant.groupingBy { it }.eachCount().values.sortedDescending()
        }
        if (buffer[0] != 'J') {
            return getMaxRankRec(buffer.substring(1), variant + buffer[0])
        }
        val variants = "AKQT98765432".map { getMaxRankRec(buffer.substring(1), variant + it) }
        return variants.maxWith(::compareLists)
    }

    fun cardValues(): List<Int> {
        return cards.map { "AKQT98765432J".indexOf(it) }
    }
}

fun solve(lines: List<String>): Int {
    val hands = lines.map { line ->
        val (cards, bidStr) = line.split(" ")
        Hand(cards, bidStr.toInt())
    }
    val sortedHands = hands.sortedWith(
        compareBy<Hand, List<Int>>(::compareLists) { it.rank }
            .then(compareByDescending(::compareLists) { it.cardValues() }))
    return sortedHands.withIndex().sumOf { v -> (v.index + 1) * v.value.bid }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
