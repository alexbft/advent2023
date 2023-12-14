package day14.part2

import bootstrap.readAllLinesFromInput

private enum class CellGroup { Open, Wall }

private fun rotateLeft(grid: List<List<Char>>): List<List<Char>> {
    return grid[0].indices.reversed().map { colIndex ->
        grid.map { row -> row[colIndex] }
    }
}

private fun rotateRight(grid: List<List<Char>>): List<List<Char>> {
    return grid[0].indices.map { colIndex ->
        grid.map { row -> row[colIndex] }.reversed()
    }
}

private fun shiftToLeft(grid: List<List<Char>>): List<List<Char>> {
    return grid.map { row ->
        var start = 0
        var prevGroup = CellGroup.Open
        var bufferRocks = 0
        val result = mutableListOf<Char>()

        fun addOpenGroup(groupLength: Int) {
            for (i in 0 until bufferRocks) {
                result.add('O')
            }
            for (i in bufferRocks until groupLength) {
                result.add('.')
            }
        }

        for ((cur, curCell) in row.withIndex()) {
            val curGroup = if (curCell == '#') CellGroup.Wall else CellGroup.Open
            if (curGroup == CellGroup.Open) {
                if (curCell == 'O') {
                    ++bufferRocks
                }
                if (prevGroup == CellGroup.Wall) {
                    start = cur
                }
            } else {
                if (prevGroup == CellGroup.Open) {
                    addOpenGroup(cur - start)
                    bufferRocks = 0
                }
                result.add('#')
            }
            prevGroup = curGroup
        }
        if (prevGroup == CellGroup.Open) {
            addOpenGroup(row.size - start)
        }
        result
    }
}

private fun format(grid: List<List<Char>>): String {
    return "\n${grid.joinToString("\n") { row -> row.joinToString("") }}"
}

private const val targetIter = 1_000_000_000

private fun iterate(grid: List<List<Char>>): List<List<Char>> {
    var cur = grid
    for (i in 0..3) {
        cur = rotateRight(shiftToLeft(cur))
    }
    return cur
}

fun solve(lines: List<String>): Int {
    var cur = rotateLeft(lines.map { it.toList() })
    val prevStates = mutableMapOf<String, Int>()
    var cycleStart = -1
    var cycleLength = -1
    for (iter in 1..1000) {
        cur = iterate(cur)
        val state = format(cur)
        if (state in prevStates) {
            cycleStart = prevStates[state]!!
            cycleLength = iter - cycleStart
            break
        }
        prevStates[state] = iter
    }
    if (cycleStart == -1) {
        throw Exception("cycle not found")
    }
    val cyclePos = (targetIter - cycleStart) % cycleLength
    for (iter in 0 until cyclePos) {
        cur = iterate(cur)
    }
    return cur.sumOf { row ->
        row.withIndex().sumOf { (i, c) ->
            if (c == 'O') row.size - i else 0
        }
    }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
