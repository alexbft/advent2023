package day17.part2

import bootstrap.readAllLinesFromInput
import java.util.PriorityQueue

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private enum class Direction(val vector: Vector2) {
    Right(Vector2(1, 0)),
    Down(Vector2(0, 1)),
    Left(Vector2(-1, 0)),
    Up(Vector2(0, -1));

    fun isOpposite(other: Direction): Boolean {
        return vector.x == -other.vector.x && vector.y == -other.vector.y
    }
}

private data class Position(val coords: Vector2, val orientation: Direction, val straightSteps: Int)

private data class QueueItem(val position: Position, val distance: Int)

private const val infinity = 1_000_000_000

private fun solveCase(rows: List<List<Int>>, startOrientation: Direction): Int {
    val w = rows[0].size
    val h = rows.size
    val start = Position(Vector2(0, 0), startOrientation, 0)
    val target = Vector2(w - 1, h - 1)
    val visited = mutableSetOf<Position>()
    val distanceMap = mutableMapOf(start to 0)
    val queue = PriorityQueue<QueueItem>(compareBy { it.distance })
    queue.add(QueueItem(start, 0))

    val minSteps = 4
    val maxSteps = 10

    while (queue.isNotEmpty()) {
        val (cur, curDist) = queue.remove()
        if (cur in visited) {
            continue
        }
        visited.add(cur)
        if (cur.coords == target && cur.straightSteps >= minSteps) {
            return curDist
        }
        for (dir in Direction.entries) {
            if (dir.isOpposite(cur.orientation)) {
                continue
            }
            if (dir != cur.orientation && cur.straightSteps < minSteps) {
                continue
            }
            val newCoords = cur.coords + dir.vector
            if (newCoords.x !in 0..<w || newCoords.y !in 0..<h) {
                continue
            }
            val newSteps = if (dir == cur.orientation) cur.straightSteps + 1 else 1
            if (newSteps > maxSteps) {
                continue
            }
            val newPos = Position(newCoords, dir, newSteps)
            val newDist = curDist + rows[newCoords.y][newCoords.x]
            val prevDist = distanceMap[newPos]
            if (prevDist == null || newDist < prevDist) {
                distanceMap[newPos] = newDist
                queue.add(QueueItem(newPos, newDist))
            }
        }
    }
    return infinity
}

fun solve(lines: List<String>): Int {
    val rows = lines.map { line -> line.map { "$it".toInt() } }
    return listOf(Direction.Right, Direction.Down).minOf { solveCase(rows, it) }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
