package day9.part1

import bootstrap.readAllLinesFromInput

private fun extrapolate(numbers: List<Int>): Int {
    if (numbers.all { it == numbers[0] }) {
        return numbers[0]
    }
    val diffs = numbers.zipWithNext().map { (a, b) -> b - a }
    return numbers.last() + extrapolate(diffs)
}

fun solve(lines: List<String>): Long {
    return lines.map { line ->
        val numbers = line.split(" ").map { it.toInt() }
        extrapolate(numbers)
    }.sumOf { it.toLong() }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
