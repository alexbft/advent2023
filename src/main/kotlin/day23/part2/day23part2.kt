package day23.part2

import bootstrap.readAllLinesFromInput

private data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

private val moves = listOf(
    Vector2(1, 0), Vector2(0, 1), Vector2(-1, 0), Vector2(0, -1)
)

private data class Edge(val from: Int, val to: Int, val length: Int)

private data class NodeGraph(
    val edges: Map<Int, List<Edge>>, val startNodeId: Int, val endNodeId: Int
)

private data class QueueItem(val pos: Vector2, val distance: Int)

private fun getNodeGraph(rows: List<String>): NodeGraph {
    val width = rows[0].length
    val height = rows.size
    val edges = mutableListOf<Edge>()
    val startNodeId = 0
    val endNodeId = 1
    var id = 2
    val nodesByPosition = mutableMapOf<Vector2, Int>()
    val startPosition = Vector2(1, 0)
    val endPosition = Vector2(width - 2, height - 1)
    nodesByPosition[startPosition] = startNodeId
    nodesByPosition[endPosition] = endNodeId
    val queue = ArrayDeque(listOf(startPosition))
    val visited = mutableSetOf(startPosition)
    while (queue.isNotEmpty()) {
        val pos = queue.removeFirst()
        val possibleMoves = moves.filter { move ->
            val next = pos + move
            next !in visited && next.x in 0..<width && next.y in 0..<height && rows[next.y][next.x] != '#'
        }
        if (possibleMoves.size > 1) {
            val nextNodeId = id++
            nodesByPosition[pos] = nextNodeId
        }
        for (move in possibleMoves) {
            val nextPos = pos + move
            queue.add(nextPos)
            visited.add(nextPos)
        }
    }
//    val debugRows = rows.map { it.toMutableList() }
//    for ((pos, nodeId) in nodesByPosition) {
//        debugRows[pos.y][pos.x] = "0123456789abcdefghijklmnopqrstuvwxyz"[nodeId]
//    }
//    println(debugRows.joinToString("\n") { it.joinToString("") })
    for ((pos, nodeId) in nodesByPosition) {
        val visited = mutableSetOf(pos)
        val queue = ArrayDeque(listOf(QueueItem(pos, 0)))
        while (queue.isNotEmpty()) {
            val (pos, dist) = queue.removeFirst()
            if (pos in nodesByPosition) {
                val nextNodeId = nodesByPosition[pos]!!
                if (nextNodeId != nodeId) {
                    edges.add(Edge(nodeId, nextNodeId, dist))
                    continue
                }
            }
            val possibleMoves = moves.filter { move ->
                val next = pos + move
                next !in visited && next.x in 0..<width && next.y in 0..<height && rows[next.y][next.x] != '#'
            }
            for (move in possibleMoves) {
                val nextPos = pos + move
                queue.add(QueueItem(nextPos, dist + 1))
                visited.add(nextPos)
            }
        }
    }
    println("DEBUG nodes count: $id")
    return NodeGraph(edges.groupBy { it.from }, startNodeId, endNodeId)
}

private fun findLongestPath(graph: NodeGraph, curNodeId: Int, visited: Long): Int? {
    if (curNodeId == graph.endNodeId) {
        return 0
    }
    var maxLength = 0
    for (edge in graph.edges[curNodeId]!!) {
        if (((1L shl edge.to) and visited) != 0L) {
            continue
        }
        val maybePath = findLongestPathCached(graph, edge.to, visited + (1L shl curNodeId))
        if (maybePath != null && maybePath + edge.length > maxLength) {
            maxLength = maybePath + edge.length
        }
    }
    return if (maxLength > 0) maxLength else null
}

private val cache = mutableMapOf<Int, MutableMap<Long, Int?>>()
// var counter = 0

private fun findLongestPathCached(graph: NodeGraph, curNodeId: Int, visited: Long): Int? {
    return cache[curNodeId]!!.getOrPut(visited) {
//        if (counter++ % 1000000 == 0) {
//            println("$counter :: Cache ${cache.values.sumOf { it.size }}")
//        }
        findLongestPath(graph, curNodeId, visited)
    }
}

fun solve(rows: List<String>): Int {
    val graph = getNodeGraph(rows)
    for (node in graph.edges.keys) {
        cache[node] = mutableMapOf()
    }

    val path = findLongestPathCached(graph, graph.startNodeId, 0L)!!
    return path
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
