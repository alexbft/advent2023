package day2.part1

import bootstrap.readAllLinesFromInput

private val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun solveFull(lines: List<String>): List<Int> {
    val gameRe = """Game (\d+): (.+)""".toRegex()
    val result = mutableListOf<Int>()
    for (line in lines) {
        val match = gameRe.matchEntire(line) ?: throw Exception("No match at $line")
        val id = match.groupValues[1].toInt()
        val gameStr = match.groupValues[2]
        val rounds = gameStr.split("; ")
        val possible = rounds.all { round ->
            val cubes = round.split(", ")
            cubes.all { cube ->
                val (num, color) = cube.split(" ")
                num.toInt() <= (limits[color] ?: 0)
            }
        }
        if (possible) {
            result.add(id)
        }
    }
    return result
}

private fun solve(lines: List<String>): Int {
    return solveFull(lines).sum()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
