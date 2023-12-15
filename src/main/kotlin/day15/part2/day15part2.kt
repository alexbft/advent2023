package day15.part2

import bootstrap.readAllLinesFromInput

private class Box(val id: Int) {
    private val lensMap = linkedMapOf<String, Int>()

    fun process(label: String, operation: Char, value: Int?) {
        when (operation) {
            '=' -> lensMap[label] = value!!
            '-' -> lensMap.remove(label)
            else -> throw Exception("Unknown operation $operation")
        }
    }

    fun focusingPower(): Int {
        return (id + 1) * lensMap.values.withIndex().sumOf { (i, v) ->
            (i + 1) * v
        }
    }
}

private fun hash(s: String): Int {
    val ascii = Charsets.US_ASCII.encode(s).array()
    var result = 0
    for (b in ascii) {
        result = (result + b) * 17 % 256
    }
    return result
}

fun solve(line: String): Int {
    val boxes = mutableMapOf<Int, Box>()
    for (command in line.split(",")) {
        val cmdRe = """(\w+)([\-=])(\d+)?""".toRegex()
        val match = cmdRe.matchEntire(command) ?: throw Exception("No match for $command")
        val label = match.groupValues[1]
        val operation = match.groupValues[2][0]
        val value = match.groups[3]?.value?.toInt()
        val labelHash = hash(label)
        val box = boxes.computeIfAbsent(labelHash) { Box(it) }
        box.process(label, operation, value)
    }
    return boxes.values.sumOf { it.focusingPower() }
}

fun main() {
    val line = readAllLinesFromInput().first()
    println(solve(line))
}
