package day5.part1

import bootstrap.readAllLinesFromInput

private data class RangeMapping(val destStart: Long, val srcStart: Long, val length: Long) {
    fun isInRange(number: Long): Boolean {
        return number in srcStart until srcStart + length
    }
    fun convert(number: Long): Long {
        if (!isInRange(number)) {
            throw Exception("Not in range")
        }
        return destStart + (number - srcStart)
    }
}

fun solve(lines: List<String>): Long {
    val blocks = lines.joinToString("\n").split("\n\n")
    val seedsRe = """seeds: (.+)""".toRegex()
    val seedsMatch = seedsRe.matchEntire(blocks[0]) ?: throw Exception("seeds: no match")
    val seeds = seedsMatch.groupValues[1].split(" ").map { it.toLong() }
    var current = seeds
    for (block in blocks.drop(1)) {
        val blockLines = block.split("\n").drop(1)
        val mappings = blockLines.map { line ->
            val (dest, src, len) = line.split(" ").map { it.toLong() }
            RangeMapping(dest, src, len)
        }
        current = current.map { seed ->
            val applicableMapping = mappings.firstOrNull { it.isInRange(seed) }
            applicableMapping?.convert(seed) ?: seed
        }
    }
    return current.min()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
