package day19.part2

import bootstrap.readAllLinesFromInput
import kotlin.math.max
import kotlin.math.min

private enum class Operator { Less, More }

private enum class Outcome { Accept, Reject, Transfer }

private data class Rule(val varName: Char, val operator: Operator, val operand: Int, val ifTrue: Outcome, val transferTo: String?) {
    fun rangeTrue(range: ValuesRange): ValuesRange {
        if (varName == '0') {
            return range
        }
        return range.limit(varName, operator, operand)
    }

    fun rangeFalse(range: ValuesRange): ValuesRange {
        if (varName == '0') {
            return emptyRange
        }
        return range.limitFalse(varName, operator, operand)
    }
}

private data class Node(val name: String, val rules: List<Rule>)

private data class ValuesRange(val byName: Map<Char, IntRange>) {
    fun limit(varName: Char, operator: Operator, operand: Int): ValuesRange {
        val range = byName[varName]!!
        return when (operator) {
            Operator.Less -> ValuesRange(byName + (varName to range.first..min(range.last, operand - 1)))
            Operator.More -> ValuesRange(byName + (varName to max(range.first, operand + 1)..range.last))
        }
    }

    fun limitFalse(varName: Char, operator: Operator, operand: Int): ValuesRange {
        val range = byName[varName]!!
        return when (operator) {
            Operator.Less -> ValuesRange(byName + (varName to max(range.first, operand)..range.last))
            Operator.More -> ValuesRange(byName + (varName to range.first..min(range.last, operand)))
        }
    }

    fun isEmpty(): Boolean {
        return byName.values.any { it.isEmpty() }
    }

    val totalCombinations: Long
        get() {
            var result = 1L
            for (range in byName.values) {
                result *= range.last - range.first + 1
            }
            return result
        }
}

private val emptyRange: ValuesRange = ValuesRange(mapOf(
    'x' to IntRange.EMPTY,
    'm' to IntRange.EMPTY,
    'a' to IntRange.EMPTY,
    's' to IntRange.EMPTY,
))

private fun recVisit(range: ValuesRange, nodesMap: Map<String, Node>, nodeId: String): Long {
    val node = nodesMap[nodeId]!!
    var curRange = range
    var result = 0L
    for (rule in node.rules) {
        if (curRange.isEmpty()) {
            break
        }
        val rangeTrue = rule.rangeTrue(curRange)
        if (!rangeTrue.isEmpty()) {
            when (rule.ifTrue) {
                Outcome.Accept -> result += rangeTrue.totalCombinations
                Outcome.Reject -> {}
                Outcome.Transfer -> result += recVisit(rangeTrue, nodesMap, rule.transferTo!!)
            }
        }
        curRange = rule.rangeFalse(curRange)
    }
    return result
}

fun solve(lines: List<String>): Long {
    val blocks = lines.joinToString("\n").split("\n\n").map { it.split("\n") }
    val nodeRe = """(\w+)\{(.+)}""".toRegex()
    val ruleRe = """(?:([xmas])([<>])(\d+):)?(\w+)""".toRegex()
    val nodes = blocks[0].map { line ->
        val match = nodeRe.matchEntire(line) ?: throw Exception("No match at $line")
        val name = match.groupValues[1]
        val rulesStrList = match.groupValues[2].split(",")
        val rules = rulesStrList.map { ruleStr ->
            val ruleMatch = ruleRe.matchEntire(ruleStr) ?: throw Exception("No match for rule $ruleStr")
            val varName: Char
            val operator: Operator
            val operand: Int
            if (ruleMatch.groups[1] != null) {
                varName = ruleMatch.groupValues[1][0]
                operator = if (ruleMatch.groupValues[2] == "<") Operator.Less else Operator.More
                operand = ruleMatch.groupValues[3].toInt()
            } else {
                varName = '0'
                operator = Operator.Less
                operand = 0
            }
            var transferTo: String? = null
            val ifTrue = when (ruleMatch.groupValues[4]) {
                "A" -> Outcome.Accept
                "R" -> Outcome.Reject
                else -> Outcome.Transfer.also { transferTo = ruleMatch.groupValues[4] }
            }
            Rule(varName, operator, operand, ifTrue, transferTo)
        }
        Node(name, rules)
    }
    val nodesMap = nodes.associateBy { it.name }

    val range = ValuesRange(mapOf(
        'x' to 1..4000,
        'm' to 1..4000,
        'a' to 1..4000,
        's' to 1..4000,
    ))
    return recVisit(range, nodesMap, "in")
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
