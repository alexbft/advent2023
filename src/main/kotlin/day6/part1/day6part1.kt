package day6.part1

import bootstrap.readAllLinesFromInput

fun solve(lines: List<String>): Int {
    val whitespace = "\\s+".toRegex()
    val times = lines[0].split(whitespace).drop(1).map { it.toInt() }
    val dist = lines[1].split(whitespace).drop(1).map { it.toInt() }
    var result = 1
    for ((t, d) in times.zip(dist)) {
        var ways = 0
        for (t0 in 1..<t) {
            if ((t - t0) * t0 > d) {
                ++ways
            }
        }
        result *= ways
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
