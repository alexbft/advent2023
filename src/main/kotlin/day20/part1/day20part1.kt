package day20.part1

import bootstrap.readAllLinesFromInput

private const val pulseCount = 1000

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
}

private class BroadcastModule(
    override val exits: List<String>
) : Module {
    override fun handlePulse(fromModule: String, pulse: Boolean) = pulse
}

private class ConjunctionModule(
    override val exits: List<String>,
    inputs: List<String>,
) : Module {
    private val inputStates = inputs.associateWith { false }.toMutableMap()

    override fun handlePulse(fromModule: String, pulse: Boolean): Boolean {
        inputStates[fromModule] = pulse
        return !inputStates.values.all { it }
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
    val inputsByModule = moduleDescriptions.keys.associateWith { name ->
        moduleDescriptions.filterValues { it.exits.contains(name) }.keys
    }
    val modules = moduleDescriptions.mapValues { (name, info) ->
        when (info.type) {
            ModuleType.FlipFlop -> FlipFlopModule(info.exits)
            ModuleType.Broadcast -> BroadcastModule(info.exits)
            ModuleType.Conjunction -> ConjunctionModule(info.exits, inputsByModule[name]!!.toList())
        }
    }

    var highPulses = 0
    var lowPulses = 0
    for (i in 0 until pulseCount) {
        val pulseQueue = ArrayDeque<Pulse>()
        pulseQueue.add(Pulse("button", "broadcaster", false))
        ++lowPulses
        while (pulseQueue.isNotEmpty()) {
            val (fromModule, destinationModule, pulse) = pulseQueue.removeFirst()
            // println("$i: $fromModule $destinationModule $pulse")
            val module = modules[destinationModule] ?: continue
            val output = module.handlePulse(fromModule, pulse)
            if (output != null) {
                if (output) {
                    highPulses += module.exits.size
                } else {
                    lowPulses += module.exits.size
                }
                module.exits.forEach { exit ->
                    pulseQueue.add(Pulse(destinationModule, exit, output))
                }
            }
        }
    }
    return highPulses.toLong() * lowPulses
}

fun main() {
    println(solve(readAllLinesFromInput(), pulseCount))
}
