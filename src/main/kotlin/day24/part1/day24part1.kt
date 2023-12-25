package day24.part1

import bootstrap.readAllLinesFromInput
import kotlin.math.abs

private data class Vector3(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
}

private data class Hailstone(val position: Vector3, val velocity: Vector3)

private fun checkCollision(stone1: Hailstone, stone2: Hailstone, testRange: LongRange): Boolean {
    // x0 + t0 * vx0 = x1 + t1 * vx1
    // y0 + t0 * vy0 = y1 + t1 * vy1
    // t1 = (x0 + t0 * vx0 - x1) / vx1
    // y0 + t0 * vy0 = y1 + (x0 + t0 * vx0 - x1) * vy1 / vx1
    // t0 = (y1 - y0 + (x0 - x1) * vy1 / vx1) / (vy0 - vx0 * vy1 / vx1)
    val vx0 = stone1.velocity.x
    val vy0 = stone1.velocity.y
    val vx1 = stone2.velocity.x
    val vy1 = stone2.velocity.y
    val x0 = stone1.position.x
    val y0 = stone1.position.y
    val x1 = stone2.position.x
    val y1 = stone2.position.y
    val b = (vy0 - vx0 * vy1 / vx1.toDouble())
    if (abs(b) < 1e-6) {
        // println("$stone1 $stone2 -> parallel")
        return false
    }
    val t0 = (y1 - y0 + (x0 - x1) * vy1 / vx1.toDouble()) / b
    if (t0 < 0) {
        // println("$stone1 $stone2 -> t0 < 0")
        return false
    }
    val t1 = (x0 + t0 * vx0 - x1) / vx1.toDouble()
    if (t1 < 0) {
        // println("$stone1 $stone2 -> t1 < 0")
        return false
    }
    val xColl = x0 + t0 * vx0
    val yColl = y0 + t0 * vy0
    // println("$stone1 $stone2 -> $xColl $yColl")
    return xColl >= testRange.first && xColl <= testRange.last && yColl >= testRange.first && yColl <= testRange.last
}

private val testRange = 200000000000000L..400000000000000L

fun solve(lines: List<String>, testRange: LongRange): Int {
    val lineRe = """(-?\d+),\s+(-?\d+),\s+(-?\d+) @\s+(-?\d+),\s+(-?\d+),\s+(-?\d+)""".toRegex()
    val stones = lines.map { line ->
        val match = lineRe.matchEntire(line) ?: throw Exception("Invalid line: $line")
        val (x, y, z, vx, vy, vz) = match.destructured
        Hailstone(Vector3(x.toLong(), y.toLong(), z.toLong()), Vector3(vx.toLong(), vy.toLong(), vz.toLong()))
    }
    val pairs = stones.flatMapIndexed { i, stone1 ->
        stones.subList(i + 1, stones.size).map { stone2 -> stone1 to stone2 }
    }
    return pairs.count { (a, b) -> checkCollision(a, b, testRange) }
}

fun main() {
    println(solve(readAllLinesFromInput(), testRange))
}
