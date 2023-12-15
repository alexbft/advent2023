package day15.part1

import bootstrap.readAllLinesFromInput

private fun hash(s: String): Int {
    val ascii = Charsets.US_ASCII.encode(s).array()
    var result = 0
    for (b in ascii) {
        result = (result + b) * 17 % 256
    }
    return result
}

fun solve(line: String): Int {
    return line.split(",").sumOf(::hash)
}

fun main() {
    val line = readAllLinesFromInput().first()
    println(solve(line))
}
