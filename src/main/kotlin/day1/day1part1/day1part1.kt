package day1.day1part1

import bootstrap.readAllLinesFromInput

fun solve(lines: List<String>): Int {
    val digits = lines.map { line ->
        line.filter { it in "0123456789" }
    }
    val nums = digits.map { line ->
        "${line.first()}${line.last()}".toInt()
    }
    return nums.sum()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
