package day3.part2

import bootstrap.readAllLinesFromInput

private data class Point(val x: Int, val y: Int)

private data class NumberWithPosition(val xRange: IntRange, val y: Int, val num: Int) {
    fun adjacentToPoint(p: Point): Boolean {
        return p.x in (xRange.first - 1..xRange.last + 1) && p.y in (y - 1..y + 1)
    }
}

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

fun solve(lines: List<String>): Int {
    val numbers = mutableListOf<NumberWithPosition>()
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
                val num = lines[y].substring(x until x2).toInt()
                numbers.add(NumberWithPosition(x until x2, y, num))
                x = x2 - 1
            }
        } while (++x < grid.w)
    }
    var result = 0
    for (y in 0 until grid.h) {
        for (x in 0 until grid.w) {
            val c = grid.safeGet(Point(x, y))
            if (c == '*') {
                val adjacentNumbers = numbers.filter { it.adjacentToPoint(Point(x, y)) }
                if (adjacentNumbers.size == 2) {
                    result += adjacentNumbers[0].num * adjacentNumbers[1].num
                }
            }
        }
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
