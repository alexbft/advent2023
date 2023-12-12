package day11.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

private data class Vector2(val x: Int, val y: Int)

fun solve(lines: List<String>, emptyRowWeight: Long): Long {
    val rowWeights = lines.map { line ->
        if (line.all { it == '.' }) emptyRowWeight else 1L
    }
    val cols = lines[0].indices.map { i ->
        lines.map { it[i] }.joinToString("")
    }
    val colWeights = cols.map { col ->
        if (col.all { it == '.' }) emptyRowWeight else 1L
    }
    val galaxyPositions = mutableListOf<Vector2>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '#') {
                galaxyPositions.add(Vector2(x, y))
            }
        }
    }
    var result = 0L
    for (p0 in galaxyPositions) {
        for (p1 in galaxyPositions) {
            if (p0 == p1) {
                continue
            }
            for (colIndex in min(p0.x, p1.x)..<max(p0.x, p1.x)) {
                result += colWeights[colIndex]
            }
            for (rowIndex in min(p0.y, p1.y)..<max(p0.y, p1.y)) {
                result += rowWeights[rowIndex]
            }
        }
    }
    return result / 2
}

fun main() {
    println(solve(readAllLinesFromInput(), 1_000_000L))
}
