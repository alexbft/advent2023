package day8.part1

import bootstrap.readAllLinesFromInput

private data class Node(val id: String, val left: String, val right: String)

const val maxSteps = 100_000_000

fun solve(lines: List<String>): Int {
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
    var current = "AAA"
    for (step in 0..<maxSteps) {
        val instruction = instructions[step % instructions.length]
        val curNode = pathMap[current]!!
        val next = if (instruction == 'L') curNode.left else curNode.right
        if (next == "ZZZ") {
            return step + 1
        }
        current = next
    }
    throw Exception("too many steps")
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
