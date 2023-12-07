package day7.part1

import bootstrap.readAllLinesFromInput

private data class Hand(val cards: String, val bid: Int) {
    val rank: Int
    init {
        val cardGroupSize = cards.groupingBy { it }.eachCount().values.sortedDescending()
        rank = when {
            cardGroupSize[0] == 5 -> 0 // 5 of a kind
            cardGroupSize[0] == 4 -> -1 // 4 of a kind
            cardGroupSize[0] == 3 && cardGroupSize[1] == 2 -> -2 // full house
            cardGroupSize[0] == 3 && cardGroupSize[1] == 1 -> -3 // 3 of a kind
            cardGroupSize[0] == 2 && cardGroupSize[1] == 2 -> -4 // two pairs
            cardGroupSize[0] == 2 && cardGroupSize[1] == 1 -> -5 // pair
            else -> -6 // high card
        }
    }

    fun cardValues(): List<Int> {
        return cards.map { "AKQJT98765432".indexOf(it) }
    }
}

fun solve(lines: List<String>): Int {
    val hands = lines.map { line ->
        val (cards, bidStr) = line.split(" ")
        Hand(cards, bidStr.toInt())
    }
    val sortedHands = hands.sortedWith(compareBy<Hand> { it.rank }.thenComparator { a, b ->
        for ((aVal, bVal) in a.cardValues().zip(b.cardValues())) {
            if (aVal != bVal) {
                return@thenComparator bVal compareTo aVal
            }
        }
        0
    })
    return sortedHands.withIndex().sumOf { v -> (v.index + 1) * v.value.bid }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
