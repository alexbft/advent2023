package day24.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.abs

private data class Vector3(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
}

private data class Vector3f(val x: Double, val y: Double, val z: Double) {
    operator fun plus(other: Vector3f) = Vector3f(x + other.x, y + other.y, z + other.z)
}

private data class Hailstone(val position: Vector3, val velocity: Vector3)

private fun getAngleDiffSum(stones: List<Hailstone>, t: Double): Double {
    val positions = stones.map { stone ->
        val x = stone.position.x + t * stone.velocity.x
        val y = stone.position.y + t * stone.velocity.y
        val z = stone.position.z + t * stone.velocity.z
        Vector3f(x, y, z)
    }
    val diffs = stones.map { stone ->
        val angle = Math.atan2(stone.position.y.toDouble(), stone.position.x.toDouble())
        val angleDiff = angle - Math.PI / 2
        abs(angleDiff)
    }
    return diffs.sum().toLong()
}


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
