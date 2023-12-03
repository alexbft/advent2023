package day1.day1part2

import bootstrap.readAllLinesFromInput

fun solve(lines: List<String>): Int {
    val digits = buildMap<String, String> {
        put("one", "1")
        put("two", "2")
        put("three", "3")
        put("four", "4")
        put("five", "5")
        put("six", "6")
        put("seven", "7")
        put("eight", "8")
        put("nine", "9")
        for (l in values.toList()) {
            put(l, l)
        }
    }
    val nums = lines.map { line ->
        val first = line.findAnyOf(digits.keys)!!.second
        val last = line.findLastAnyOf(digits.keys)!!.second
        "${digits[first]}${digits[last]}".toInt()
    }
    return nums.sum()
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
