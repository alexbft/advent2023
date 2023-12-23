package day21.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private const val maxSteps = 26501365
private val directions = listOf(Vector2(0, -1), Vector2(0, 1), Vector2(-1, 0), Vector2(1, 0))

private class Grid(val rows: List<String>) {
    val width = rows[0].length
    val height = rows.size
    val tileSize = Vector2(width, height)

    fun isWall(pos: Vector2): Boolean {
        val localX = (pos.x % width).let { if (it < 0) it + width else it }
        val localY = (pos.y % height).let { if (it < 0) it + height else it }
        return rows[localY][localX] == '#'
    }
}

private fun getTile(pos: Vector2, tileSize: Vector2): Vector2 {
    val tileX = (pos.x / tileSize.x).let { if (pos.x < 0) it - 1 else it }
    val tileY = (pos.y / tileSize.y).let { if (pos.y < 0) it - 1 else it }
    return Vector2(tileX, tileY)
}

private data class TileData(val cycleStart: Int, val cycle: List<Int>, val head: List<Int>) {
    private val headStart get() = cycleStart - head.size

    fun calcAtStep(step: Int): Int {
        if (step < headStart) {
            return 0
        }
        if (step < cycleStart) {
            return head[step - headStart]
        }
        return cycle[(step - cycleStart) % cycle.size]
    }
}

private fun calcTileData(countHistory: Map<Int, Int>): TileData {
    val minStep = countHistory.keys.min()
    val values = countHistory.entries.sortedBy { it.key }.map { it.value }
    val prev = mutableMapOf<Int, Int>()
    var cycleStartIndex = 0
    var cycleLength = 0
    val maxCount = values.max()
    for ((i, v) in values.withIndex()) {
        if (v in prev && v == maxCount) {
            cycleStartIndex = prev[v]!!
            cycleLength = i - cycleStartIndex
            break
        }
        prev[v] = i
    }
    val cycle = values.subList(cycleStartIndex, cycleStartIndex + cycleLength)
    val head = values.subList(0, cycleStartIndex)
    return TileData(minStep + cycleStartIndex, cycle, head)
}

private fun checkTileDataMatches(countByTileHistory: Map<Vector2, Map<Int, Int>>, tile0: Vector2, tile1: Vector2): Int {
    val tileData0 = calcTileData(countByTileHistory[tile0]!!)
    val tileData1 = calcTileData(countByTileHistory[tile1]!!)
    if (tileData0.cycle != tileData1.cycle) {
        throw Exception("Cycle mismatch at $tile0 & $tile1: ${tileData0.cycle} != ${tileData1.cycle}")
    }
    if (tileData0.head != tileData1.head) {
        throw Exception("Head mismatch at $tile0 & $tile1: ${tileData0.head} != ${tileData1.head}")
    }
    return tileData1.cycleStart - tileData0.cycleStart
}

private fun calcPositionsInRow(row: Int, tileData: Map<Vector2, TileData>, stepsDiff: Int, stepsTotal: Int): Long {
    if (stepsTotal <= 0) {
        return 0
    }
    if (row > 3) {
        return calcPositionsInRow(3, tileData, stepsDiff, stepsTotal - stepsDiff * (row - 3))
    }
    if (row < -3) {
        return calcPositionsInRow(-3, tileData, stepsDiff, stepsTotal - stepsDiff * (abs(row) - 3))
    }
    val startCol = if (abs(row) == 3) 3 else 4
    var result = 0L
    result += tileData[Vector2(0, row)]!!.calcAtStep(stepsTotal)
    for (i in 1 until startCol) {
        result += tileData[Vector2(i, row)]!!.calcAtStep(stepsTotal)
        result += tileData[Vector2(-i, row)]!!.calcAtStep(stepsTotal)
    }
    val dataRightBound = tileData[Vector2(startCol - 1, row)]!!
    val safeRepeatRight = max((stepsTotal - dataRightBound.cycleStart) / (stepsDiff * 2) - 1, 0)
    val count0 = dataRightBound.calcAtStep(stepsTotal - stepsDiff)
    val count1 = dataRightBound.calcAtStep(stepsTotal - stepsDiff * 2)
    result += (count0 + count1) * safeRepeatRight
    var rightShift = safeRepeatRight * 2 + 1
    while (stepsTotal > stepsDiff * rightShift) {
        result += dataRightBound.calcAtStep(stepsTotal - stepsDiff * rightShift)
        ++rightShift
    }

    val dataLeftBound = tileData[Vector2(-(startCol - 1), row)]!!
    val safeRepeatLeft = max((stepsTotal - dataLeftBound.cycleStart) / (stepsDiff * 2) - 1, 0)
    val count0Left = dataLeftBound.calcAtStep(stepsTotal - stepsDiff)
    val count1Left = dataLeftBound.calcAtStep(stepsTotal - stepsDiff * 2)
    result += (count0Left + count1Left) * safeRepeatLeft
    var leftShift = safeRepeatLeft * 2 + 1
    while (stepsTotal > stepsDiff * leftShift) {
        result += dataLeftBound.calcAtStep(stepsTotal - stepsDiff * leftShift)
        ++leftShift
    }

    return result
}

fun solve(lines: List<String>, maxSteps: Int): Long {
    // Find start position
    val start = lines.withIndex().flatMap { (y, line) ->
        line.withIndex().mapNotNull { (x, c) ->
            if (c == 'S') Vector2(x, y) else null
        }
    }[0]
    val grid = Grid(lines)
    val startPositions = listOf(start)
    var positions = startPositions.toSet()
    val countByTileHistory = mutableMapOf<Vector2, MutableMap<Int, Int>>()
    val simSteps = min(maxSteps, grid.width * 7)
    for (step in 1..simSteps) {
        println("Step $step/$simSteps: ${positions.size}")
        val newPositions = mutableSetOf<Vector2>()
        for (pos in positions) {
            for (dir in directions) {
                val newPos = pos + dir
                if (grid.isWall(newPos)) continue
                newPositions.add(newPos)
            }
        }
        positions = newPositions
        val countByTile = positions.groupingBy { getTile(it, grid.tileSize) }.eachCount()
        for ((tile, count) in countByTile) {
            countByTileHistory.getOrPut(tile) { mutableMapOf() }[step] = count
        }
    }

    if (maxSteps == simSteps) {
        return positions.size.toLong()
    }
    val tileData = countByTileHistory.mapValues { calcTileData(it.value) }
    val stepsDiff = checkTileDataMatches(countByTileHistory, Vector2(3, 0), Vector2(4, 0))
    println("steps diff: $stepsDiff")

    var result = calcPositionsInRow(0, tileData, stepsDiff, maxSteps)
    var row = 0
    while (true) {
        row += 1
        val down = calcPositionsInRow(row, tileData, stepsDiff, maxSteps)
        val up = calcPositionsInRow(-row, tileData, stepsDiff, maxSteps)
        if (up == 0L && down == 0L) {
            break
        }
        result += up + down
    }

    return result
}

fun main() {
    println(solve(readAllLinesFromInput(), maxSteps))
}
