typealias Stack = ArrayDeque<Char>

fun Stack.push(element: Char) = addLast(element)
fun Stack.pop(): Char? = removeLastOrNull()
fun Stack.peek(): Char? = lastOrNull()

typealias StackMap = MutableMap<Int, Stack>

fun StackMap.print() = forEach { (id, stack) -> println("$id: $stack") }

fun topCrates(input: String, reverse: Boolean): String =
  String(
    makeStackMap(input)
      .applyMoves(input, reverse)
      .values.map { it.peek()!! }
      .toCharArray()
  )

fun makeStackMap(input: String): StackMap {
  val lines = input.lines()
  val stackIdLineIndex = lines.indexOfFirst { it.startsWith(" 1") }
  val stackIdLine = lines[stackIdLineIndex]
  val stackLines = lines.subList(0, stackIdLineIndex).reversed()
  val stackMap: StackMap = mutableMapOf()
  stackIdLine.toCharArray().forEachIndexed { index, char ->
    if (char.isDigit()) {
      val stackId = char.digitToInt()
      stackMap[stackId] = Stack()
      stackLines.forEach { line ->
        line.getOrNull(index)?.let { char ->
          if (!char.isWhitespace()) {
            stackMap[stackId]!!.push(char)
          }
        }
      }
    }
  }
  return stackMap
}

fun StackMap.applyMoves(input: String, reverse: Boolean): StackMap {
  val lines = input.lines()
  val firstMoveLineIndex = lines.indexOfFirst { it.startsWith("move") }
  lines.subList(firstMoveLineIndex, lines.size).forEach { moveLine ->
    val moveParts = moveLine.split(' ')
    val qty = moveParts[1].toInt()
    val src = moveParts[3].toInt()
    val dst = moveParts[5].toInt()
    if (reverse) {
      (1..qty).map { this[src]!!.pop()!! }
        .reversed()
        .forEach { this[dst]!!.push(it) }
    } else {
      repeat(qty) { this[src]!!.pop()?.let { this[dst]!!.push(it) } }
    }
  }
  return this
}