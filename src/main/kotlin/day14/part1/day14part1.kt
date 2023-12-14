package day14.part1

import bootstrap.readAllLinesFromInput

private enum class CellGroup { Open, Wall }

fun solve(lines: List<String>): Int {
    val cols = lines[0].indices.map { i ->
        lines.map { it[i] }
    }
    var result = 0
    for (col in cols) {
        var start = 0
        var prevGroup = CellGroup.Open
        var bufferRocks = 0
        for (cur in col.indices) {
            val curCell = col[cur]
            val curGroup = if (curCell == '#') CellGroup.Wall else CellGroup.Open
            when {
                curGroup == CellGroup.Open -> {
                    if (curCell == 'O') {
                        ++bufferRocks
                    }
                    if (prevGroup == CellGroup.Wall) {
                        start = cur
                    }
                }
                curGroup == CellGroup.Wall && prevGroup == CellGroup.Open -> {
                    for (i in 0 until bufferRocks) {
                        result += col.size - (start + i)
                    }
                    bufferRocks = 0
                }
            }
            prevGroup = curGroup
        }
        for (i in 0 until bufferRocks) {
            result += col.size - (start + i)
        }
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
