package day11.part1

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

private data class Vector2(val x: Int, val y: Int)

fun solve(lines: List<String>): Int {
    val rowWeights = lines.map { line ->
        if (line.all { it == '.' }) 2 else 1
    }
    val cols = lines[0].indices.map { i ->
        lines.map { it[i] }.joinToString("")
    }
    val colWeights = cols.map { col ->
        if (col.all { it == '.' }) 2 else 1
    }
    val galaxyPositions = mutableListOf<Vector2>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '#') {
                galaxyPositions.add(Vector2(x, y))
            }
        }
    }
    var result = 0
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
    println(solve(readAllLinesFromInput()))
}
