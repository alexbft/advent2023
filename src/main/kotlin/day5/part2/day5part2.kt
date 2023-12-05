package day5.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

private data class RangeMapping(val destStart: Long, val srcRange: LongRange) {
    constructor(destStart: Long, srcStart: Long, length: Long) : this(destStart, srcStart until srcStart + length)

    fun convertIntersection(range: LongRange): LongRange {
        val srcIntersection = intersect(srcRange, range)
        if (!srcIntersection.isEmpty()) {
            val dStart = srcIntersection.first - srcRange.first
            val dEnd = srcIntersection.last - srcRange.first
            return destStart + dStart..destStart + dEnd
        }
        return LongRange.EMPTY
    }
}

fun intersect(range1: LongRange, range2: LongRange): LongRange {
    val intersectionStart = max(range1.first, range2.first)
    val intersectionEnd = min(range1.last, range2.last)
    return if (intersectionStart <= intersectionEnd) intersectionStart..intersectionEnd else LongRange.EMPTY
}

fun cutOut(range: LongRange, cut: LongRange): List<LongRange> {
    val beforeCut = range.first until cut.first
    val afterCut = cut.last + 1..range.last
    return listOf(beforeCut, afterCut).filter { !it.isEmpty() }
}

fun solve(lines: List<String>): Long {
    val blocks = lines.joinToString("\n").split("\n\n")
    val seedsRe = """seeds: (.+)""".toRegex()
    val seedsMatch = seedsRe.matchEntire(blocks[0]) ?: throw Exception("seeds: no match")
    val seedsData = seedsMatch.groupValues[1].split(" ").map { it.toLong() }
    val seedRanges = seedsData.chunked(2).map { (start, length) -> start until start + length }
    var current = seedRanges
    for (block in blocks.drop(1)) {
        val blockLines = block.split("\n").drop(1)
        val mappings = blockLines.map { line ->
            val (dest, src, len) = line.split(" ").map { it.toLong() }
            RangeMapping(dest, src, len)
        }
        // println("Current: $current")
        // println("Mappings: $mappings")
        val next = mutableListOf<LongRange>()
        for (seedRange in current) {
            var seedRangeList = listOf(seedRange)
            for (mapping in mappings) {
                if (seedRangeList.isEmpty()) {
                    break
                }
                val nextRangeList = mutableListOf<LongRange>()
                for (innerSeedRange in seedRangeList) {
                    val intersection = intersect(mapping.srcRange, innerSeedRange)
                    if (intersection.isEmpty()) {
                        nextRangeList.add(innerSeedRange)
                    } else {
                        next.add(mapping.convertIntersection(innerSeedRange))
                        nextRangeList.addAll(cutOut(innerSeedRange, intersection))
                    }
                }
                seedRangeList = nextRangeList
            }
            next.addAll(seedRangeList)
        }
        current = next
    }
    return current.minOf { it.first }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
