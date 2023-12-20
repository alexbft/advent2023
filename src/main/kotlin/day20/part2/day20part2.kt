package day20.part2

import bootstrap.readAllLinesFromInput

private const val pulseCount = 1000000

private interface Module {
    fun handlePulse(fromModule: String, pulse: Boolean): Boolean?
    val exits: List<String>
}

private class FlipFlopModule(
    override val exits: List<String>
) : Module {
    var state: Boolean = false

    override fun handlePulse(fromModule: String, pulse: Boolean): Boolean? {
        if (pulse) return null
        state = !state
        return state
    }

    override fun toString(): String {
        return "%${if (state) "1" else "0"}"
    }
}

private class BroadcastModule(
    override val exits: List<String>
) : Module {
    override fun handlePulse(fromModule: String, pulse: Boolean) = pulse

    override fun toString(): String {
        return ""
    }
}

private class ConjunctionModule(
    override val exits: List<String>,
    inputs: List<String>,
) : Module {
    private val inputStates = inputs.associateWith { false }.toMutableMap()
    private var lowPulses = inputs.size

    override fun handlePulse(fromModule: String, pulse: Boolean): Boolean {
        if (inputStates[fromModule] != pulse) {
            if (pulse) {
                lowPulses--
            } else {
                lowPulses++
            }
            inputStates[fromModule] = pulse
        }
        return lowPulses != 0
    }

    override fun toString(): String {
        return "&${inputStates.values.joinToString("") { if (it) "1" else "0" } }"
    }
}

private enum class ModuleType {
    FlipFlop,
    Broadcast,
    Conjunction,
}

private data class ModuleInfo(
    val name: String,
    val type: ModuleType,
    val exits: List<String>,
)

private data class Pulse(
    val fromModule: String,
    val destinationModule: String,
    val pulse: Boolean,
)

private fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun solve(lines: List<String>, pulseCount: Int): Long {
    val moduleRegex = """([%&])?(\w+) -> (.+)""".toRegex()
    val moduleDescriptions = lines.map { line ->
        val match = moduleRegex.matchEntire(line) ?: throw IllegalArgumentException("Invalid line: $line")
        val (type, name, exits) = match.destructured
        ModuleInfo(
            name = name,
            type = when (type) {
                "%" -> ModuleType.FlipFlop
                "&" -> ModuleType.Conjunction
                else -> ModuleType.Broadcast
            },
            exits = exits.split(", ")
        )
    }.associateBy { it.name }
    //println(moduleDescriptions)
    val inputsByModule = moduleDescriptions.values.flatMap { it.exits }.toSet().associateWith { name ->
        moduleDescriptions.filterValues { it.exits.contains(name) }.keys
    }
    println("rx: ${inputsByModule["rx"]}")
    println("dh: ${inputsByModule["dh"]}")
    val modules = moduleDescriptions.mapValues { (name, info) ->
        when (info.type) {
            ModuleType.FlipFlop -> FlipFlopModule(info.exits)
            ModuleType.Broadcast -> BroadcastModule(info.exits)
            ModuleType.Conjunction -> ConjunctionModule(info.exits, inputsByModule[name]!!.toList())
        }
    }

    val debugLowIters = mutableMapOf<String, List<Int>>()
    for (i in 0 until pulseCount) {
//        println("Step $i")
//        for ((name, module) in modules) {
//            println("$name: $module")
//        }
        val pulseQueue = ArrayDeque<Pulse>()
        pulseQueue.add(Pulse("button", "broadcaster", false))
        while (pulseQueue.isNotEmpty()) {
            val (fromModule, destinationModule, pulse) = pulseQueue.removeFirst()
//            if (destinationModule == "rx" && !pulse) {
//                return i + 1
//            }
            // println("$i: $fromModule $destinationModule $pulse")
            val module = modules[destinationModule] ?: continue
            val output = module.handlePulse(fromModule, pulse)
            if (module is ConjunctionModule && output == false) {
                val list = debugLowIters.getOrDefault(destinationModule, emptyList())
                debugLowIters[destinationModule] = if (list.size > 10) {
                    list.drop(1) + i
                } else {
                    list + i
                }
            }
            if (output != null) {
                module.exits.forEach { exit ->
                    pulseQueue.add(Pulse(destinationModule, exit, output))
                }
            }
        }
    }
    for ((name, iters) in debugLowIters) {
        println("$name: ${inputsByModule[name]} ${iters.zipWithNext().map { it.second - it.first } }")
    }
    // Run once, then copy all cycle lengths for conjunction modules into the list below
    return listOf(3739L, 3761L, 3797L, 3889L).reduce(::lcm)
}

fun main() {
    println(solve(readAllLinesFromInput(), pulseCount))
}
