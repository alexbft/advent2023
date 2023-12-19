package day18.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun times(factor: Int) = Vector2(x * factor, y * factor)
}

private enum class Direction(val vector: Vector2) {
    Right(Vector2(1, 0)),
    Down(Vector2(0, 1)),
    Left(Vector2(-1, 0)),
    Up(Vector2(0, -1));
}

private data class Edge(val direction: Direction, val length: Int)

private class TrenchRow {
    private val pointsUp = mutableSetOf<Int>()
    private val pointsDown = mutableSetOf<Int>()

    fun addUp(x: Int) {
        pointsUp.add(x)
    }

    fun addDown(x: Int) {
        pointsDown.add(x)
    }

    fun copy(): TrenchRow {
        val result = TrenchRow()
        for (p in pointsUp) {
            result.addUp(p)
        }
        for (p in pointsDown) {
            result.addDown(p)
        }
        return result
    }

    val totalLength: Long
        get() {
            if (pointsUp.size % 2 != 0) {
                throw Exception("points up odd")
            }
            if (pointsDown.size % 2 != 0) {
                throw Exception("points down odd")
            }
            val allPointSet = mutableSetOf<Int>()
            allPointSet.addAll(pointsUp)
            allPointSet.addAll(pointsDown)
            val allPoints = allPointSet.sorted()
            var prev = 0
            var countUp = 0
            var countDown = 0
            var result = 0L
            for (p in allPoints) {
                if (countUp % 2 != 0 || countDown % 2 != 0) {
                    result += (p - prev)
                    prev = p
                } else {
                    prev = p - 1
                }
                if (p in pointsUp) {
                    ++countUp
                }
                if (p in pointsDown) {
                    ++countDown
                }
            }
            return result
        }
}

private class TrenchRowCollection {
    val rowMap = mutableMapOf<Int, TrenchRow>()

    private fun rows(y0: Int, y1: Int): List<TrenchRow> {
        val yMin = min(y0, y1)
        val yMax = max(y0, y1)
        return buildList {
            val ys = rowMap.keys.toMutableList()
            ys.sort()
            if (ys.isEmpty() || ys[0] > yMin) {
                rowMap[yMin] = TrenchRow()
                ys.add(0, yMin)
            }
            var curIndex = 0
            while (ys[curIndex] < yMin) {
                ++curIndex
            }
            if (ys[curIndex] > yMin) {
                rowMap[yMin] = rowMap[ys[curIndex - 1]]!!.copy()
            } else {
                ++curIndex
            }
            add(rowMap[yMin]!!)
            while (curIndex < ys.size && ys[curIndex] <= yMax) {
                add(rowMap[ys[curIndex]]!!)
                ++curIndex
            }
            if (curIndex >= ys.size) {
                rowMap[yMax + 1] = TrenchRow()
            } else {
                if (ys[curIndex] > yMax + 1) {
                    rowMap[yMax + 1] = rowMap[ys[curIndex - 1]]!!.copy()
                }
            }
        }
    }

    fun addUp(y0: Int, y1: Int, x: Int) {
        for (row in rows(y0, y1)) {
            row.addUp(x)
        }
    }

    fun addDown(y0: Int, y1: Int, x: Int) {
        for (row in rows(y0, y1)) {
            row.addDown(x)
        }
    }

    val totalArea: Long
        get() {
            val ys = rowMap.keys.sorted()
            var result = 0L
            for ((y0, y1) in ys.zipWithNext()) {
                result += (y1 - y0).toLong() * rowMap[y0]!!.totalLength
            }
            return result
        }
}

fun solve(lines: List<String>): Long {
    val edgeRe = """[RLUD] \d+ \(#(.{5})([0123])\)""".toRegex()
    val edges = lines.map { line ->
        val match = edgeRe.matchEntire(line) ?: throw Exception("No match at $line")
        val direction = when (match.groupValues[2]) {
            "0" -> Direction.Right
            "1" -> Direction.Down
            "2" -> Direction.Left
            "3" -> Direction.Up
            else -> throw Exception("wrong direction at $line")
        }
        val length = match.groupValues[1].toInt(16)
        Edge(direction, length)
    }

    var currentPosition = Vector2(0, 0)
    val rows = TrenchRowCollection()
    for (edge in edges) {
        if (edge.direction == Direction.Up || edge.direction == Direction.Down) {
            val startRow = currentPosition.y
            if (edge.direction == Direction.Up) {
                rows.addUp(startRow, startRow, currentPosition.x)
            } else {
                rows.addDown(startRow, startRow, currentPosition.x)
            }
            if (edge.length > 1) {
                val row0 = startRow + edge.direction.vector.y
                val row1 = startRow + (edge.direction.vector * (edge.length - 1)).y
                rows.addUp(row0, row1, currentPosition.x)
                rows.addDown(row0, row1, currentPosition.x)
            }
            currentPosition += edge.direction.vector * edge.length
            val endRow = currentPosition.y
            if (edge.direction == Direction.Up) {
                rows.addDown(endRow, endRow, currentPosition.x)
            } else {
                rows.addUp(endRow, endRow, currentPosition.x)
            }
        } else {
            currentPosition += edge.direction.vector * edge.length
        }
    }

    return rows.totalArea
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
