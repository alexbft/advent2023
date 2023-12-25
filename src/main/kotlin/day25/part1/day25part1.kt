package day25.part1

import bootstrap.readAllLinesFromInput

typealias Graph = List<List<Int>>
typealias Edge = Pair<Int, Int>

private fun findBridge(graph: Graph, removedEdge1: Edge, removedEdge2: Edge): Edge? {
    var time = 0
    val visited = Array(graph.size) { false }
    val discoveryTime = Array(graph.size) { -1 }
    val low = Array(graph.size) { -1 }
    var result: Edge? = null

    fun rec(node: Int, parent: Int) {
        visited[node] = true
        discoveryTime[node] = time
        low[node] = time
        time++
        for (child in graph[node]) {
            if (child == parent) {
                continue
            }
            if (child == removedEdge1.second && node == removedEdge1.first || child == removedEdge1.first && node == removedEdge1.second) {
                continue
            }
            if (child == removedEdge2.second && node == removedEdge2.first || child == removedEdge2.first && node == removedEdge2.second) {
                continue
            }
            if (!visited[child]) {
                rec(child, node)
                low[node] = minOf(low[node], low[child])
                if (low[child] > discoveryTime[node]) {
                    result = Edge(node, child)
                    return
                }
            } else {
                low[node] = minOf(low[node], discoveryTime[child])
            }
        }
    }

    rec(0, -1)
    return result
}

private fun findComponentSizes(graph: Graph, removedEdges: List<Edge>): Map<Int, Int> {
    val componentMap = mutableMapOf<Int, Int>()
    var componentCounter = 0
    for (node in graph.indices) {
        if (node !in componentMap) {
            componentCounter++
            val queue = ArrayDeque(listOf(node))
            componentMap[node] = componentCounter
            while (queue.isNotEmpty()) {
                val curNode = queue.removeFirst()
                for (child in graph[curNode]) {
                    val edge = curNode to child
                    val reversedEdge = child to curNode
                    if (edge in removedEdges || reversedEdge in removedEdges) {
                        continue
                    }
                    if (child !in componentMap) {
                        componentMap[child] = componentCounter
                        queue.add(child)
                    }
                }
            }
        }
    }
    return componentMap.values.groupingBy { it }.eachCount()
}

fun solve(lines: List<String>): Int {
    val edgesStr = lines.flatMap { line ->
        val (src, destList) = line.split(": ", limit = 2)
        val dests = destList.split(" ")
        dests.map { src to it }
    }
    println("Edges count: ${edgesStr.size}")
    val nodes = edgesStr.flatMap { listOf(it.first, it.second) }.toSet()
    println("Nodes count: ${nodes.size}")
    val indexByNode = nodes.withIndex().associate { it.value to it.index }
    val nodeByIndex = indexByNode.entries.associate { it.value to it.key }
    val edges = edgesStr.map { indexByNode[it.first]!! to indexByNode[it.second]!! }
    val graphMap = mutableMapOf<Int, MutableList<Int>>()
    for (edge in edges) {
        graphMap.getOrPut(edge.first) { mutableListOf() }.add(edge.second)
        graphMap.getOrPut(edge.second) { mutableListOf() }.add(edge.first)
    }
    val graph = graphMap.entries.sortedBy { it.key }.map { it.value }
    outer@for (i in edges.indices) {
        println("Checking edge $i/${edges.size}")
        for (j in i + 1 until edges.size) {
            val maybeBridge = findBridge(graph, edges[i], edges[j])
            if (maybeBridge != null) {
                val removedEdges = listOf(edges[i], edges[j], maybeBridge)
                println("Bridge found: ${removedEdges.map { nodeByIndex[it.first]!! to nodeByIndex[it.second]!! } }")
                val sizes = findComponentSizes(graph, removedEdges)
                if (sizes.size > 1) {
                    println("Component sizes: $sizes")
                    return sizes.values.reduce { a, b -> a * b }
                }
                else {
                    println("Component sizes: $sizes -> fail!!!")
                }
            }
        }
    }
    throw Exception("No bridge found")
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
