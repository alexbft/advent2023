package day4.part2

import bootstrap.readAllLinesFromInput

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
        for (i in 0 until wins) {
            if (i < additionalCopies.size) {
                additionalCopies[i] += copies
            } else {
                additionalCopies.addLast(copies)
            }
        }
        result += copies
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
