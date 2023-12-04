package day4.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

fun solve(lines: List<String>): Long {
    val cardRe = """Card(\s+)(\d+): (.+)""".toRegex()
    var result = 0L
    val additionalCopies = ArrayDeque<Int>()
    for (line in lines) {
        val match = cardRe.matchEntire(line) ?: throw Exception("No match: $line")
        val numsRaw = match.groupValues[3]
        val (numsWinStr, numsCardStr) = numsRaw.split(" | ")
        val numsWin = numsWinStr.split(" ").filter { it != "" }.map { it.toInt() }.toSet()
        val numsCard = numsCardStr.split(" ").filter { it != "" }.map { it.toInt() }
        val wins = numsCard.count { it in numsWin }
        val copies = 1 + (if (additionalCopies.isEmpty()) 0 else additionalCopies.removeFirst())
        val pile1 = min(wins, additionalCopies.size)
        val pile2 = max(0, wins - additionalCopies.size)
        for (i in 0 until pile1) {
            additionalCopies[i] += copies
        }
        for (i in 0 until pile2) {
            additionalCopies.addLast(copies)
        }
        result += copies
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
