package day3.part1

import bootstrap.readAllLinesFromInput

private data class Point(val x: Int, val y: Int)

private class CharGrid(val lines: List<String>) {
    val w = if (lines.isNotEmpty()) lines[0].length else 0
    val h = lines.size

    fun isInside(p: Point): Boolean {
        return p.x in (0 until w) && p.y in (0 until h)
    }

    fun safeGet(p: Point): Char {
        return if (isInside(p)) lines[p.y][p.x] else ' '
    }
}

private fun isAdjacentToSymbol(grid: CharGrid, xRange: IntRange, y: Int): Boolean {
    for (y1 in y - 1..y + 1) {
        for (x1 in xRange.first - 1..xRange.last + 1) {
            val c = grid.safeGet(Point(x1, y1))
            if (c != '.' && c != ' ' && c !in '0'..'9') {
                return true
            }
        }
    }
    return false
}

fun solve(lines: List<String>): Int {
    var result = 0
    val grid = CharGrid(lines)
    for (y in 0 until grid.h) {
        var x = 0
        do {
            val c = grid.safeGet(Point(x, y))
            if (c in '0'..'9') {
                var x2 = x
                while (grid.safeGet(Point(x2, y)) in '0'..'9') {
                    ++x2
                }
                if (isAdjacentToSymbol(grid, x until x2, y)) {
                    val num = lines[y].substring(x until x2).toInt()
                    result += num
                }
                x = x2 - 1
            }
        } while (++x < grid.w)
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
