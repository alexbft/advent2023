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
) {
    fun hasPathWithoutNode(nodeId: Int): Boolean {
        val visited = mutableSetOf(nodeId, startNodeId)
        val queue = ArrayDeque(listOf(startNodeId))
        while (queue.isNotEmpty()) {
            val curNodeId = queue.removeFirst()
            if (curNodeId == endNodeId) {
                return true
            }
            val connections = edges[curNodeId]!!.filter { edge ->
                edge.to !in visited
            }
            for (edge in connections) {
                queue.add(edge.to)
                visited.add(edge.to)
            }
        }
        return false
    }
}

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
    println("DEBUG nodes: ${id}")
    return NodeGraph(edges.groupBy { it.from }, startNodeId, endNodeId)
}

private data class Path(val nodes: List<Int>, val length: Int)

private fun findLongestPath(graph: NodeGraph, curNodeId: Int, visited: Set<Int>): Path? {
    if (curNodeId == graph.endNodeId) {
        return Path(listOf(curNodeId), 0)
    }
    if (graph.edges[graph.endNodeId]!!.all { it.to in visited }) {
        return null
    }
    val connections = graph.edges[curNodeId]!!.filter { edge ->
        edge.to !in visited
    }
    var maxLength = 0
    var maxPath: Path? = null
    for (edge in connections) {
        val maybePath = findLongestPathCached(graph, edge.to, visited + curNodeId)
        if (maybePath != null && maybePath.length + edge.length > maxLength) {
            maxLength = maybePath.length + edge.length
            maxPath = maybePath
        }
    }
    if (maxPath == null) return null
    val nodes = listOf(curNodeId) + maxPath.nodes
    return Path(nodes, maxLength)
}

private data class CacheKey(val curNodeId: Int, val visited: Set<Int>)

private val cache = mutableMapOf<CacheKey, Path?>()

private fun findLongestPathCached(graph: NodeGraph, curNodeId: Int, visited: Set<Int>): Path? {
    val key = CacheKey(curNodeId, visited)
    return cache.getOrPut(key) {
        findLongestPath(graph, curNodeId, visited)
    }
}

fun solve(rows: List<String>): Int {
    val graph = getNodeGraph(rows)
    //println(graph.edges)
    val nodes = graph.edges.keys
    for (node in nodes) {
        if (node == graph.startNodeId || node == graph.endNodeId) {
            continue
        }
        if (!graph.hasPathWithoutNode(node)) {
            println("DEBUG node $node is a bridge")
            continue
        }
    }

//    val path = findLongestPathCached(graph, graph.startNodeId, emptySet())!!
//    println(path)
//    return path.length
    return 0
}

fun main() {
    println(solve(readAllLinesFromInput()))
}
