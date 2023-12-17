package day17.part1

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

fun solve(lines: List<String>): Int {
    val w = lines[0].length
    val h = lines.size
    val rows = lines.map { line -> line.map { "$it".toInt() } }
    val start = Position(Vector2(0, 0), Direction.Right, 0)
    val target = Vector2(w - 1, h - 1)
    val visited = mutableSetOf<Position>()
    val distanceMap = mutableMapOf(start to 0)
    val queue = PriorityQueue<QueueItem>(compareBy { it.distance })
    queue.add(QueueItem(start, 0))
    while (queue.isNotEmpty()) {
        val (cur, curDist) = queue.remove()
        if (cur in visited) {
            continue
        }
        visited.add(cur)
        if (cur.coords == target) {
            return curDist
        }
        for (dir in Direction.entries) {
            if (dir.isOpposite(cur.orientation)) {
                continue
            }
            val newCoords = cur.coords + dir.vector
            if (newCoords.x !in 0..<w || newCoords.y !in 0..<h) {
                continue
            }
            val newSteps = if (dir == cur.orientation) cur.straightSteps + 1 else 1
            if (newSteps > 3) {
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
    throw Exception("unreachable")
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
