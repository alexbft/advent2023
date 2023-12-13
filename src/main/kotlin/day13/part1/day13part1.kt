package day13.part1

import bootstrap.readAllLinesFromInput
import kotlin.math.min

private fun encode(rows: List<String>): List<Int> {
    return rows.map { row ->
        var temp = 0
        for (c in row) {
            temp = temp * 2 + if (c == '#') 1 else 0
        }
        temp
    }
}

private fun findReflection(numbers: List<Int>): Int? {
    for (dividingLine in 1..<numbers.size) {
        val part1 = numbers.subList(0, dividingLine)
        val part2 = numbers.subList(dividingLine, numbers.size)
        val minSize = min(part1.size, part2.size)
        val part1tail = part1.takeLast(minSize)
        val part2head = part2.take(minSize)
        if (part1tail == part2head.asReversed()) {
            return dividingLine
        }
    }
    return null
}

fun solveBlock(rows: List<String>): Int {
    val cols = rows[0].indices.map { x ->
        rows.map { it[x] }.joinToString("")
    }
    val maybeResult = findReflection(encode(rows))
    if (maybeResult != null) {
        return maybeResult * 100
    }
    val colsResult = findReflection(encode(cols))
    return colsResult ?: throw Exception("No solution for: \n${rows.joinToString("\n")}")
}

fun solve(lines: List<String>): Int {
    val blocks = lines.joinToString("\n").split("\n\n").map { it.split("\n") }
    return blocks.sumOf(::solveBlock)
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
