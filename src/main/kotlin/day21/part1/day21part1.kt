package day21.part1

import bootstrap.readAllLinesFromInput

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private data class WalkState(val pos: Vector2, val steps: Int)

private const val maxSteps = 64

fun solve(lines: List<String>, maxSteps: Int): Int {
    val w = lines[0].length
    val h = lines.size
    // Find start position
    val start = lines.withIndex().flatMap { (y, line) ->
        line.withIndex().mapNotNull { (x, c) ->
            if (c == 'S') Vector2(x, y) else null
        }
    }[0]
    val startState = WalkState(start, 0)
    val visited = mutableSetOf(startState)
    val queue = ArrayDeque(listOf(startState))
    while (queue.isNotEmpty()) {
        val (pos, steps) = queue.removeFirst()
        if (steps >= maxSteps) {
            break
        }
        for (dir in listOf(Vector2(0, -1), Vector2(0, 1), Vector2(-1, 0), Vector2(1, 0))) {
            val newPos = pos + dir
            if (newPos.x !in 0..<w || newPos.y !in 0..<h || lines[newPos.y][newPos.x] == '#') continue
            val newWalkState = WalkState(newPos, steps + 1)
            if (newWalkState in visited) continue
            visited.add(newWalkState)
            queue.add(newWalkState)
        }
    }
    return queue.size + 1
}

fun main() {
    println(solve(readAllLinesFromInput(), maxSteps))
}
