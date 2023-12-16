package day16.part1

import bootstrap.readAllLinesFromInput

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private enum class Direction(val vector: Vector2) {
    Right(Vector2(1, 0)),
    Down(Vector2(0, 1)),
    Left(Vector2(-1, 0)),
    Up(Vector2(0, -1));

    val isHorizontal: Boolean
        get() = vector.x != 0

    val isVertical: Boolean
        get() = vector.y != 0
}

private data class Position(val coords: Vector2, val orientation: Direction)

private class Grid(private val rows: List<String>) {
    val w = rows[0].length
    val h = rows.size

    fun isInside(p: Vector2) = p.x in 0..<w && p.y in 0..<h

    fun getExits(pos: Position): List<Direction> {
        val result = when (val c = rows[pos.coords.y][pos.coords.x]) {
            '.' -> listOf(pos.orientation)
            '/' -> listOf(
                when (pos.orientation) {
                    Direction.Right -> Direction.Up
                    Direction.Down -> Direction.Left
                    Direction.Left -> Direction.Down
                    Direction.Up -> Direction.Right
                }
            )

            '\\' -> listOf(
                when (pos.orientation) {
                    Direction.Right -> Direction.Down
                    Direction.Down -> Direction.Right
                    Direction.Left -> Direction.Up
                    Direction.Up -> Direction.Left
                }
            )

            '-' -> if (pos.orientation.isHorizontal)
                listOf(pos.orientation)
            else
                listOf(Direction.Left, Direction.Right)

            '|' -> if (pos.orientation.isVertical)
                listOf(pos.orientation)
            else
                listOf(Direction.Up, Direction.Down)

            else -> throw Exception("Invalid char $c at ${pos.coords}")
        }
        return result.filter { isInside(pos.coords + it.vector) }
    }
}

fun solve(lines: List<String>): Int {
    val grid = Grid(lines)
    val start = Position(Vector2(0, 0), Direction.Right)
    val visited = mutableSetOf(start)
    val queue = ArrayDeque(listOf(start))
    while (queue.isNotEmpty()) {
        val cur = queue.removeFirst()
        val exits = grid.getExits(cur)
        for (dir in exits) {
            val next = Position(cur.coords + dir.vector, dir)
            if (next !in visited) {
                visited.add(next)
                queue.add(next)
            }
        }
    }
    return visited.map { it.coords }.toSet().size
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
