package day9.part2

import bootstrap.readAllLinesFromInput

private fun extrapolateFirst(numbers: List<Int>): Int {
    if (numbers.all { it == numbers[0] }) {
        return numbers[0]
    }
    val diffs = numbers.zipWithNext().map { (a, b) -> b - a }
    return numbers.first() - extrapolateFirst(diffs)
}

fun solve(lines: List<String>): Long {
    return lines.map { line ->
        val numbers = line.split(" ").map { it.toInt() }
        extrapolateFirst(numbers)
    }.sumOf { it.toLong() }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
