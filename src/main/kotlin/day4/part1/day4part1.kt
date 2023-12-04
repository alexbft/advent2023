package day4.part1

import bootstrap.readAllLinesFromInput

fun solve(lines: List<String>): Int {
    val cardRe = """Card(\s+)(\d+): (.+)""".toRegex()
    var result = 0
    for (line in lines) {
        val match = cardRe.matchEntire(line) ?: throw Exception("No match: $line")
        val numsRaw = match.groupValues[3]
        val (numsWinStr, numsCardStr) = numsRaw.split(" | ")
        val numsWin = numsWinStr.split(" ").filter { it != "" }.map { it.toInt() }.toSet()
        val numsCard = numsCardStr.split(" ").filter { it != "" }.map { it.toInt() }
        val wins = numsCard.count { it in numsWin }
        if (wins > 0) {
            result += (1 shl (wins - 1))
        }
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
