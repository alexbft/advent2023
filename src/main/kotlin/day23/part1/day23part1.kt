package day23.part1

import bootstrap.readAllLinesFromInput

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)

    fun opposite() = Vector2(-x, -y)
}

private val moves = listOf(
    Vector2(1, 0),
    Vector2(0, 1),
    Vector2(-1, 0),
    Vector2(0, -1)
)

private fun findLongestPath(rows: List<String>, start: Vector2, end: Vector2, prevMove: Vector2): Int? {
    if (start == end) return 0
    val width = rows[0].length
    val height = rows.size
    var counter = 0
    var current = start
    var _prevMove = prevMove
    while (current != end) {
        val allowedMoves = getAllowedMoves(rows[current.y][current.x])
        val possibleMoves = allowedMoves.filter { move ->
            val next = current + move
            move != _prevMove.opposite() &&
                    next.x in 0..<width &&
                    next.y in 0..<height &&
                    rows[next.y][next.x] != '#'
        }
        if (possibleMoves.isEmpty()) return null
        if (possibleMoves.size > 1) {
            return possibleMoves.mapNotNull { move ->
                val next = current + move
                findLongestPath(rows, next, end, move)
            }.maxOrNull()?.let { it + counter + 1 }
        }
        val move = possibleMoves.single()
        current += move
        _prevMove = move
        counter++
    }
    return counter
}

fun solve(rows: List<String>): Int {
    val width = rows[0].length
    val height = rows.size
    val start = Vector2(1, 0)
    val end = Vector2(width - 2, height - 1)

    return findLongestPath(rows, start, end, Vector2(0, 1))!!
}

private fun getAllowedMoves(cell: Char) =
    when (cell) {
        '.' -> moves
        '>' -> listOf(Vector2(1, 0))
        '<' -> listOf(Vector2(-1, 0))
        '^' -> listOf(Vector2(0, -1))
        'v' -> listOf(Vector2(0, 1))
        else -> throw Exception("Unknown cell: $cell")
    }

fun main() {
    println(solve(readAllLinesFromInput()))
}
