package day18.part1

import bootstrap.readAllLinesFromInput

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private enum class Direction(val vector: Vector2) {
    Right(Vector2(1, 0)),
    Down(Vector2(0, 1)),
    Left(Vector2(-1, 0)),
    Up(Vector2(0, -1));
}

private data class Edge(val direction: Direction, val length: Int)

fun solve(lines: List<String>): Int {
    val edgeRe = """([RLUD]) (\d+) \(#.{6}\)""".toRegex()
    val edges = lines.map { line ->
        val match = edgeRe.matchEntire(line) ?: throw Exception("No match at $line")
        val direction = when (match.groupValues[1]) {
            "R" -> Direction.Right
            "L" -> Direction.Left
            "U" -> Direction.Up
            "D" -> Direction.Down
            else -> throw Exception("wrong direction at $line")
        }
        val length = match.groupValues[2].toInt()
        Edge(direction, length)
    }
    var currentPosition = Vector2(0, 0)
    val filled = mutableSetOf(currentPosition)
    for (edge in edges) {
        for (i in 0 until edge.length) {
            currentPosition += edge.direction.vector
            filled.add(currentPosition)
        }
    }
    val minX = filled.minOf { it.x } - 1
    val maxX = filled.maxOf { it.x } + 1
    val minY = filled.minOf { it.y } - 1
    val maxY = filled.maxOf { it.y } + 1
    val outside = mutableSetOf(Vector2(minX, minY))
    val queue = ArrayDeque(listOf(Vector2(minX, minY)))
    while (queue.isNotEmpty()) {
        val cur = queue.removeFirst()
        for (dir in Direction.entries) {
            val next = cur + dir.vector
            if (next.x !in minX..maxX || next.y !in minY..maxY) {
                continue
            }
            if (next in filled || next in outside) {
                continue
            }
            outside.add(next)
            queue.add(next)
        }
    }
    val totalArea = (maxX - minX + 1) * (maxY - minY + 1)
    return totalArea - outside.size
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
