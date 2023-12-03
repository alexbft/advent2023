package day2.part2

import bootstrap.readAllLinesFromInput

fun solveFull(lines: List<String>): List<Int> {
    val gameRe = """Game (\d+): (.+)""".toRegex()
    val result = mutableListOf<Int>()
    for (line in lines) {
        val maxColorMap = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        val match = gameRe.matchEntire(line) ?: throw Exception("No match at $line")
        val gameStr = match.groupValues[2]
        val rounds = gameStr.split("; ")
        for (round in rounds) {
            val cubes = round.split(", ")
            for (cube in cubes) {
                val (numS, color) = cube.split(" ")
                val num = numS.toInt()
                if (maxColorMap[color]!! < num) {
                    maxColorMap[color] = num
                }
            }
        }
        val product = maxColorMap.values.reduce { a, b -> a * b }
        result.add(product)
    }
    return result
}

private fun solve(lines: List<String>): Int {
    return solveFull(lines).sum()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
