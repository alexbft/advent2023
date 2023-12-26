package day24.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.abs
import kotlin.math.round

private data class Vector3(val x: Double, val y: Double, val z: Double)

private data class Hailstone(val position: Vector3, val velocity: Vector3)

private data class LinearEquation(val coeff: List<Double>, val constant: Double)

private enum class Vars(val index: Int) {
    X(0),
    Y(1),
    Z(2),
    Vx(3),
    Vy(4),
    Vz(5),
}

//private fun printEq(a: Array<DoubleArray>, b: DoubleArray) {
//    for (i in a.indices) {
//        for (j in a[i].indices) {
//            print("${a[i][j]} * x$j + ")
//        }
//        println("= ${b[i]}")
//    }
//}

fun solve(lines: List<String>): Long {
    val lineRe = """(-?\d+),\s+(-?\d+),\s+(-?\d+) @\s+(-?\d+),\s+(-?\d+),\s+(-?\d+)""".toRegex()
    val stones = lines.map { line ->
        val match = lineRe.matchEntire(line) ?: throw Exception("Invalid line: $line")
        val (x, y, z, vx, vy, vz) = match.destructured
        Hailstone(Vector3(x.toDouble(), y.toDouble(), z.toDouble()), Vector3(vx.toDouble(), vy.toDouble(), vz.toDouble()))
    }

    val equations = stones.zipWithNext().flatMap { (stone, stone2) ->
        // Vyi * X + (-Vxi) * Y + (-Yi) * Vx + Xi * Vy + (-1) * XVy + 1 * YVx = Xi * Vyi - Yi * Vxi
        // Subtract the equations pairwise to get rid of X * Vy and Y * Vx
        val coeffArrayXY = Array(Vars.entries.size) { 0.0 }
        coeffArrayXY[Vars.X.index] = stone.velocity.y - stone2.velocity.y
        coeffArrayXY[Vars.Y.index] = -stone.velocity.x + stone2.velocity.x
        coeffArrayXY[Vars.Vx.index] = -stone.position.y + stone2.position.y
        coeffArrayXY[Vars.Vy.index] = stone.position.x - stone2.position.x
        val constantXY1 = stone.position.x * stone.velocity.y - stone.position.y * stone.velocity.x
        val constantXY2 = stone2.position.x * stone2.velocity.y - stone2.position.y * stone2.velocity.x
        val constantXY = constantXY1 - constantXY2

        // Do the same for X and Z
        val coeffArrayXZ = Array(Vars.entries.size) { 0.0 }
        coeffArrayXZ[Vars.X.index] = stone.velocity.z - stone2.velocity.z
        coeffArrayXZ[Vars.Z.index] = -stone.velocity.x + stone2.velocity.x
        coeffArrayXZ[Vars.Vx.index] = -stone.position.z + stone2.position.z
        coeffArrayXZ[Vars.Vz.index] = stone.position.x - stone2.position.x
        val constantXZ1 = stone.position.x * stone.velocity.z - stone.position.z * stone.velocity.x
        val constantXZ2 = stone2.position.x * stone2.velocity.z - stone2.position.z * stone2.velocity.x
        val constantXZ = constantXZ1 - constantXZ2

        listOf(
            LinearEquation(coeffArrayXY.toList(), constantXY),
            LinearEquation(coeffArrayXZ.toList(), constantXZ),
        )
    }
    println(equations.size)

    // Solve equations with Gauss method
    val n = Vars.entries.size
    val m = equations.size
    val a = Array(m) { DoubleArray(n) }
    val b = DoubleArray(m)
    for (i in equations.indices) {
        val equation = equations[i]
        for (j in Vars.entries.indices) {
            a[i][j] = equation.coeff[j]
        }
        b[i] = equation.constant
    }
    //printEq(a, b)
    for (i in 0 until n) {
        var k = i
        for (j in i + 1 until m) {
            if (abs(a[j][i]) > abs(a[k][i])) {
                k = j
            }
        }
        val tmp = a[i]
        a[i] = a[k]
        a[k] = tmp
        val tmp2 = b[i]
        b[i] = b[k]
        b[k] = tmp2
        for (j in i + 1 until m) {
            val coef = a[j][i] / a[i][i]
            for (k2 in i until n) {
                a[j][k2] -= a[i][k2] * coef
            }
            b[j] -= b[i] * coef
        }
//        println("Step $i")
//        printEq(a, b)
    }
    val x = DoubleArray(n)
    for (i in n - 1 downTo 0) {
        var sum = 0.0
        for (j in i + 1 until n) {
            sum += a[i][j] * x[j]
        }
        x[i] = (b[i] - sum) / a[i][i]
    }
    println(x.toList())
    return (round(x[0]) + round(x[1]) + round(x[2])).toLong()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
