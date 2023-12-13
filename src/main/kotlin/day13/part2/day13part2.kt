package day13.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.min

private fun encode(rows: List<List<Char>>): List<Int> {
    return rows.map { row ->
        var temp = 0
        for (c in row) {
            temp = temp * 2 + if (c == '#') 1 else 0
        }
        temp
    }
}

private fun findReflection(numbers: List<Int>, ignoreResult: Int?): Int? {
    for (dividingLine in 1..<numbers.size) {
        val part1 = numbers.subList(0, dividingLine)
        val part2 = numbers.subList(dividingLine, numbers.size)
        val minSize = min(part1.size, part2.size)
        val part1tail = part1.takeLast(minSize)
        val part2head = part2.take(minSize)
        if (part1tail == part2head.asReversed() && dividingLine != ignoreResult) {
            return dividingLine
        }
    }
    return null
}

fun solveBlock(rows: List<List<Char>>, ignoreResult: Int?): Int? {
    val cols = rows[0].indices.map { x ->
        rows.map { it[x] }
    }
    val ignoreRowResult = if (ignoreResult != null && ignoreResult >= 100) ignoreResult / 100 else null
    val maybeResult = findReflection(encode(rows), ignoreRowResult)
    if (maybeResult != null) {
        return maybeResult * 100
    }
    return findReflection(encode(cols), ignoreResult)
}

private fun format(rows: List<String>): String {
    return "\n${rows.joinToString("\n")}"
}

fun fixBlock(rows: List<String>): Int {
    val mutRows = rows.map { it.toMutableList() }
    val firstResult = solveBlock(mutRows, null) ?: throw Exception("No solution for ${format(rows)}")
    for (row in mutRows) {
        for (i in row.indices) {
            val prev = row[i]
            row[i] = if (prev == '#') '.' else '#'
            val maybeResult = solveBlock(mutRows, firstResult)
            if (maybeResult != null) {
                return maybeResult
            }
            row[i] = prev
        }
    }
    throw Exception("No fix for ${format(rows)}")
}

fun solve(lines: List<String>): Int {
    val blocks = lines.joinToString("\n").split("\n\n").map { it.split("\n") }
    return blocks.sumOf(::fixBlock)
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
