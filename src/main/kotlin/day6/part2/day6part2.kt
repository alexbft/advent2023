package day6.part2

import bootstrap.readAllLinesFromInput

fun solve(lines: List<String>): Int {
    val whitespace = "\\s+".toRegex()
    val time = lines[0].split(whitespace).drop(1).joinToString("").toLong()
    val dist = lines[1].split(whitespace).drop(1).joinToString("").toLong()
    var result = 0
    for (t0 in 1..<time) {
        if ((time - t0) * t0 > dist) {
            ++result
        }
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
