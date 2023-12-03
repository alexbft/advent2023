package bootstrap

fun readAllLinesFromInput(): List<String> {
    return generateSequence(::readLine).toList()
}
