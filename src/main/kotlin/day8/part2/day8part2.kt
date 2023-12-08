package day8.part2

import bootstrap.readAllLinesFromInput

private data class Node(val id: String, val left: String, val right: String)

private data class State(val nodeId: String, val instructionIndex: Int)
private data class StepRecord(val nodeId: String, val step: Int)

private class Traverser(val startNode: String, stateMap: Map<State, Int>, lastState: State, lastStep: Int) {
    val cycleStart = stateMap[lastState]!!
    val cycleLength = lastStep - cycleStart
    val cycle = stateMap.entries.map { entry -> StepRecord(entry.key.nodeId, entry.value) }.sortedBy { it.step }
    private val cycleStartIndex = cycle.indexOfFirst { rec -> rec.step == cycleStart }

    private var index = 0
    private var cycleIndex = 0

    val currentStep: Long
        get() = cycle[index].step + cycleIndex * cycleLength.toLong()

    fun nextStep(): Long {
        val result = currentStep
        index += 1
        if (index >= cycle.size) {
            index = cycleStartIndex
            cycleIndex += 1
        }
        return result
    }

    override fun toString(): String {
        return "Traverser(start=${startNode} cycle=${cycle} cycleStart=${cycleStart} cycleLength=${cycleLength})"
    }
}

private const val maxSteps = 1_000_000

private fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}

private fun greatestCommonDivisor(a: Long, b: Long): Long {
    var x = a
    var y = b
    while (y > 0) {
        x = y.also { y = x % y }
    }
    return x
}

fun solve(lines: List<String>): Long {
    val blocks = lines.joinToString("\n").split("\n\n")
    val instructions = blocks[0]
    val pathMap = buildMap {
        for (line in blocks[1].split("\n")) {
            val pathRe = """(\w{3}) = \((\w{3}), (\w{3})\)""".toRegex()
            val match = pathRe.matchEntire(line) ?: throw Exception("No match: $line")
            val (id, left, right) = match.groupValues.drop(1)
            val node = Node(id, left, right)
            put(node.id, node)
        }
    }
    val startNodes = pathMap.keys.filter { it.endsWith("A") }
    val traverserMap = mutableMapOf<String, Traverser>()
    for (startNode in startNodes) {
        var current = startNode
        val stateMap = mutableMapOf<State, Int>()
        for (step in 0..<maxSteps) {
            val curNode = pathMap[current]!!
            val instructionIndex = step % instructions.length
            val instruction = instructions[instructionIndex]
            current = if (instruction == 'L') curNode.left else curNode.right
            if (current.endsWith("Z")) {
                val state = State(current, instructionIndex)
                if (state in stateMap) {
                    traverserMap[startNode] = Traverser(startNode, stateMap, state, step + 1)
                    //println(traverserMap[startNode])
                    break
                }
                stateMap[state] = step + 1
            }
        }
        if (traverserMap[startNode] == null) {
            throw Exception("too many steps for node $startNode")
        }
    }
    // The input should be a simple case where there is exactly 1 Z-node in the cycle and the cycle does not have a head
    val isSimple = traverserMap.values.all { traverser -> traverser.cycle.size == 1 && traverser.cycleStart == traverser.cycleLength }
    if (isSimple) {
        val cycleLengths = traverserMap.values.map { traverser -> traverser.cycleLength.toLong() }
        return cycleLengths.reduce(::leastCommonMultiple)
    }
    // Trying to solve the hard way... (at least should work on a test case)
    val traversers = traverserMap.values.toList()
    for (iter in 0..1_000_000_000L) {
        if (traversers.map { it.currentStep }.toSet().size == 1) {
            // found the solution
            return traversers[0].currentStep
        }
        val minTraverser = traversers.minBy { it.currentStep }
        minTraverser.nextStep()
    }
    throw Exception("Too many steps")
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
