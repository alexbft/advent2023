package day22.part1

import bootstrap.readAllLinesFromInput

private data class Vector3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
    companion object {
        fun parse(s: String): Vector3 {
            val (x, y, z) = s.split(',').map { it.toInt() }
            return Vector3(x, y, z)
        }
    }
}

private data class Cube(val pos: Vector3, val size: Vector3) {
    val pos1: Vector3
        get() = pos + size - Vector3(1, 1, 1)

    fun shift(shift: Vector3) = Cube(pos + shift, size)
}

private class Tetris {
    val maxX = 10
    val maxY = 10
    val layers = mutableListOf<Array<Boolean>>()
    val cubes = mutableSetOf<Cube>()

    fun addCube(cube: Cube) {
        cubes.add(cube)
        while (layers.size <= cube.pos1.z) {
            layers.add(Array(maxY * maxX) { false })
        }
        for (z in cube.pos.z..cube.pos1.z) {
            for (y in cube.pos.y..cube.pos1.y) {
                for (x in cube.pos.x..cube.pos1.x) {
                    layers[z][y * maxX + x] = true
                }
            }
        }
    }

    fun removeCube(cube: Cube) {
        cubes.remove(cube)
        for (z in cube.pos.z..cube.pos1.z) {
            for (y in cube.pos.y..cube.pos1.y) {
                for (x in cube.pos.x..cube.pos1.x) {
                    layers[z][y * maxX + x] = false
                }
            }
        }
    }

    fun canFitCube(cube: Cube): Boolean {
        //println("debug canFitCube $cube")
        if (cube.pos.z < 0) return false
        for (z in cube.pos.z..cube.pos1.z) {
            if (z >= layers.size) continue
            for (y in cube.pos.y..cube.pos1.y) {
                for (x in cube.pos.x..cube.pos1.x) {
                    if (layers[z][y * maxX + x]) return false
                }
            }
        }
        return true
    }
}

private val shiftDown = Vector3(0, 0, -1)

fun solve(lines: List<String>): Int {
    val cubes = lines.map { line ->
        val (pos0, pos1) = line.split("~").map { Vector3.parse(it) }
        val pmin = Vector3(minOf(pos0.x, pos1.x), minOf(pos0.y, pos1.y), minOf(pos0.z, pos1.z))
        val pmax = Vector3(maxOf(pos0.x, pos1.x), maxOf(pos0.y, pos1.y), maxOf(pos0.z, pos1.z))
        val size = pmax - pmin + Vector3(1, 1, 1)
        Cube(pmin, size)
    }
    val sortedCubes = cubes.sortedBy { it.pos.z }
    val tetris = Tetris()
    for (cube in sortedCubes) {
        val zStart = tetris.layers.size
        var shiftedCube = Cube(Vector3(cube.pos.x, cube.pos.y, zStart), cube.size)
        while (true) {
            val candidateCube = shiftedCube.shift(shiftDown)
            if (tetris.canFitCube(candidateCube)) {
                shiftedCube = candidateCube
            } else {
                break
            }
        }
        tetris.addCube(shiftedCube)
    }
    val sortedCubesInTetris = tetris.cubes.sortedBy { it.pos.z }
    var result = 0
    for ((i, cube) in sortedCubesInTetris.withIndex()) {
        tetris.removeCube(cube)
        var isEssential = false
        for (cube2 in sortedCubesInTetris.subList(i + 1, sortedCubesInTetris.size)) {
            tetris.removeCube(cube2)
            if (tetris.canFitCube(cube2.shift(shiftDown))) {
                isEssential = true
                tetris.addCube(cube2)
                break
            }
            tetris.addCube(cube2)
        }
        if (!isEssential) {
            ++result
        }
        tetris.addCube(cube)
    }
    return result
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
