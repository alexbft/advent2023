package day10.part2

import bootstrap.readAllLinesFromInput
import java.util.EnumSet

private enum class Direction {
    right, down, left, up;

    val opposite: Direction
        get() = when (this) {
            right -> left
            down -> up
            left -> right
            up -> down
        }
}

private data class Cell(val exits: EnumSet<Direction>)

private val emptyCell = Cell(EnumSet.noneOf(Direction::class.java))

private val glyphMap = buildMap<Char, EnumSet<Direction>> {
    put('-', EnumSet.of(Direction.left, Direction.right))
    put('|', EnumSet.of(Direction.up, Direction.down))
    put('L', EnumSet.of(Direction.up, Direction.right))
    put('7', EnumSet.of(Direction.left, Direction.down))
    put('J', EnumSet.of(Direction.left, Direction.up))
    put('F', EnumSet.of(Direction.right, Direction.down))
    put('.', EnumSet.noneOf(Direction::class.java))
    put('S', EnumSet.noneOf(Direction::class.java))
}

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Vector2 {
        return when (direction) {
            Direction.right -> Vector2(x + 1, y)
            Direction.down -> Vector2(x, y + 1)
            Direction.left -> Vector2(x - 1, y)
            Direction.up -> Vector2(x, y - 1)
        }
    }
}

private class Grid(private val lines: List<List<Cell>>) {
    val h = lines.size
    val w = lines[0].size

    fun get(position: Vector2) = lines[position.y][position.x]

    fun safeGet(position: Vector2) = if (isInside(position)) get(position) else emptyCell

    private fun isInside(position: Vector2) = position.x in 0..<w && position.y in 0..<h

    fun countIntersections(start: Vector2, moveDir: Direction, loop: Set<Vector2>, countDir: Direction): Int {
        var cursor = start
        var loopIntersections = 0
        while (isInside(cursor)) {
            if (cursor in loop && countDir in get(cursor).exits) {
                ++loopIntersections
            }
            cursor += moveDir
        }
        return loopIntersections
    }
}

fun solve(lines: List<String>): Int {
    val gridLines = lines.map { line ->
        line.map { c ->
            Cell(glyphMap[c] ?: throw Exception("unknown char $c"))
        }.toMutableList()
    }
    val grid = Grid(gridLines)
    val startPos = lines.withIndex().firstNotNullOf { (y, line) ->
        val maybeX = line.indexOf('S')
        if (maybeX == -1) null else Vector2(maybeX, y)
    }
    val startDirs = Direction.entries.filter { dir ->
        dir.opposite in grid.safeGet(startPos + dir).exits
    }
    gridLines[startPos.y][startPos.x] = Cell(EnumSet.copyOf(startDirs))
    val loop = mutableSetOf(startPos)
    val queue = ArrayDeque(listOf(startPos))
    while (queue.isNotEmpty()) {
        val cur = queue.removeFirst()
        for (dir in grid.get(cur).exits) {
            val newPos = cur + dir
            if (newPos !in loop) {
                loop.add(newPos)
                queue.addLast(newPos)
            }
        }
    }
    var insidePointsCount = 0
    for (y in 0..<grid.h) {
        for (x in 0..<grid.w) {
            val point = Vector2(x, y)
            if (point in loop) {
                continue
            }
            val intersections = listOf(
                grid.countIntersections(point, Direction.up, loop, Direction.left),
                grid.countIntersections(point, Direction.up, loop, Direction.right),
                grid.countIntersections(point, Direction.down, loop, Direction.left),
                grid.countIntersections(point, Direction.down, loop, Direction.right),
                grid.countIntersections(point, Direction.right, loop, Direction.up),
                grid.countIntersections(point, Direction.right, loop, Direction.down),
                grid.countIntersections(point, Direction.left, loop, Direction.up),
                grid.countIntersections(point, Direction.left, loop, Direction.down),
            )
            val intersectionsOdd = intersections.map { it % 2 }
            if (intersectionsOdd.any { it != intersectionsOdd[0] }) {
                throw Exception("assertion failed at $point: $intersections")
            }
            if (intersectionsOdd[0] != 0) {
                ++insidePointsCount
            }
        }
    }
    return insidePointsCount
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
