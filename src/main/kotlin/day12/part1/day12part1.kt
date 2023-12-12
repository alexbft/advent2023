package day12.part1

import bootstrap.readAllLinesFromInput

private fun recCount(buf: String, line: String, groups: List<Int>): Long {
    if (buf.isNotEmpty()) {
        return if (buf[0] == '?') {
            recCount(buf.substring(1), "$line.", groups) +
                    recCount(buf.substring(1), "$line#", groups)
        } else {
            recCount(buf.substring(1), line + buf[0], groups)
        }
    }
    val countedGroups = mutableListOf<Int>()
    var counter = 0
    for (i in line.indices) {
        if (line[i] == '#') {
            ++counter
        } else if (i > 0 && line[i - 1] == '#') {
            countedGroups.add(counter)
            counter = 0
        }
    }
    if (counter > 0) {
        countedGroups.add(counter)
    }
    return if (countedGroups == groups) 1 else 0
}

fun solve(lines: List<String>): Long {
    return lines.sumOf { line ->
        val (row, groupsS) = line.split(" ", limit = 2)
        val groups = groupsS.split(",").map { it.toInt() }
        recCount(row, "", groups)
    }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
