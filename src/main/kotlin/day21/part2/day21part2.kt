package day21.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.abs
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

private data class TileData(val cycleStart: Int, val cycle: List<Int>, val head: List<Int>)

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

private fun checkTileDataMatches(countByTileHistory: Map<Vector2, Map<Int, Int>>, tile0: Vector2, tile1: Vector2) {
    val tileData0 = calcTileData(countByTileHistory[tile0]!!)
    val tileData1 = calcTileData(countByTileHistory[tile1]!!)
    if (tileData0.cycle != tileData1.cycle) {
        println("Cycle mismatch at $tile0 & $tile1: ${tileData0.cycle} != ${tileData1.cycle}")
    }
    if (tileData0.head != tileData1.head) {
        println("Head mismatch at $tile0 & $tile1: ${tileData0.head} != ${tileData1.head}")
    }
    println(tileData1.cycleStart - tileData0.cycleStart)
}

fun solve(lines: List<String>, maxSteps: Int): Int {
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
        println("Step $step: ${positions.size}")
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
            if (abs(tile.x) > 4 || abs(tile.y) > 4) continue
            countByTileHistory.getOrPut(tile) { mutableMapOf() }[step] = count
        }
    }
    if (maxSteps == simSteps) {
        return positions.size
    }
    checkTileDataMatches(countByTileHistory, Vector2(3, 0), Vector2(4, 0))
    checkTileDataMatches(countByTileHistory, Vector2(-3, 0), Vector2(-4, 0))
    checkTileDataMatches(countByTileHistory, Vector2(0, 3), Vector2(0, 4))
    checkTileDataMatches(countByTileHistory, Vector2(0, -3), Vector2(0, -4))
    listOf(Vector2(1, 1), Vector2(1, -1), Vector2(-1, 1), Vector2(-1, -1)).forEach { i ->
        checkTileDataMatches(countByTileHistory, Vector2(i.x * 2, i.y * 2), Vector2(i.x * 3, i.y * 3))
    }
    return positions.size
}

fun main() {
    println(solve(readAllLinesFromInput(), maxSteps))
}
