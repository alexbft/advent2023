package day19.part1

import bootstrap.readAllLinesFromInput

private enum class Operator { Less, More }

private enum class Outcome { Accept, Reject, Transfer }

private data class Rule(val varName: Char, val operator: Operator, val operand: Int, val ifTrue: Outcome, val transferTo: String?) {
    fun match(values: Map<Char, Int>): Boolean {
        if (varName == '0') {
            return true
        }
        val operand0 = values[varName]!!
        return if (operator == Operator.Less) operand0 < operand else operand0 > operand
    }
}

private data class Node(val name: String, val rules: List<Rule>)

private fun parseAndCalc(nodes: Map<String, Node>, line: String): Int {
    val lineRe = """\{(.+)}""".toRegex()
    val partRe = """([xmas])=(\d+)""".toRegex()
    val match = lineRe.matchEntire(line) ?: throw Exception("no match for vars $line")
    val varParts = match.groupValues[1].split(",")
    val values = varParts.associate { part ->
        val partMatch = partRe.matchEntire(part) ?: throw Exception("no match for part $part")
        val varName = partMatch.groupValues[1][0]
        val varValue = partMatch.groupValues[2].toInt()
        varName to varValue
    }
    var curNode = nodes["in"]!!
    while (true) {
        for (rule in curNode.rules) {
            if (rule.match(values)) {
                when (rule.ifTrue) {
                    Outcome.Accept -> return values.values.sum()
                    Outcome.Reject -> return 0
                    Outcome.Transfer -> curNode = nodes[rule.transferTo!!]!!
                }
                break
            }
        }
    }
}

fun solve(lines: List<String>): Int {
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

    return blocks[1].sumOf { line -> parseAndCalc(nodesMap, line) }
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
