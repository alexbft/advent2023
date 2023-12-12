package day12.part2

import bootstrap.readAllLinesFromInput

private data class State(val buf: String, val nextGroup: Int, val groups: List<Int>)

private val cache = mutableMapOf<State, Long>()

private fun recCount(buf: String, nextGroup: Int, groups: List<Int>): Long {
    if (buf.isEmpty()) {
        return if (nextGroup == 0 && groups.isEmpty() || groups.size == 1 && groups[0] == nextGroup) 1 else 0
    }
    val state = State(buf, nextGroup, groups)
    if (state in cache) {
        return cache[state]!!
    }
    val bufS = buf.substring(1)
    fun recFilled() = recCount(bufS, nextGroup + 1, groups)
    fun recEmpty() = if (nextGroup == 0)
        recCount(bufS, nextGroup, groups)
    else
        if (groups.isEmpty() || groups[0] != nextGroup) 0 else recCount(bufS, 0, groups.drop(1))
    val result = when (buf[0]) {
        '?' -> recFilled() + recEmpty()
        '#' -> recFilled()
        '.' -> recEmpty()
        else -> throw Exception("unexpected char at $buf")
    }
    cache[state] = result
    return result
}

fun solve(lines: List<String>): Long {
    return lines.sumOf { line ->
        val (row, groupsS) = line.split(" ", limit = 2)
        val groups = groupsS.split(",").map { it.toInt() }
        val expRow = "$row?$row?$row?$row?$row"
        val expGroups = buildList {
            for (i in 0..<5) addAll(groups)
        }
        recCount(expRow, 0, expGroups)
    }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
