import java.io.File

fun main() {
  val testInput = """[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]"""
  val realInput = File("src/Day13.txt").readText()

  val part1TestOutput = sumOfCorrectPairIndices(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 13)

  val part1RealOutput = sumOfCorrectPairIndices(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = decoderKey(testInput)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 140)

  val part2RealOutput = decoderKey(realInput)
  println("Part 2 Real Output: $part2RealOutput")
}

/**
 * Part 1
 *
 * Determine how many pairs of packets are in the right order.
 * Packet data consists of lists and integers.
 * If both values are integers, the lower integer should come first.
 * If both values are lists, compare the first value of each list, then the second value, and so on.
 */
fun sumOfCorrectPairIndices(input: String): Int {
  val correctIndices = mutableListOf<Int>()
  input.split("\n\n").forEachIndexed { index, section ->
    val lines = section.lines()
    val leftPacket = ListValue(lines[0])
    val rightPacket = ListValue(lines[1])
    if (leftPacket.compareTo(rightPacket) < 1) {
      correctIndices.add(index + 1)
    }
  }
  return correctIndices.sum()
}

/**
 * Part 2
 */
fun decoderKey(input: String): Int {
  val decoderPacket1 = ListValue("[[2]]")
  val decoderPacket2 = ListValue("[[6]]")
  val inputPackets = input.lines().filter { it.isNotEmpty() }.map { ListValue(it) }
  val packets = (inputPackets + decoderPacket1 + decoderPacket2).sorted()
  return (packets.indexOf(decoderPacket1) + 1) * (packets.indexOf(decoderPacket2) + 1)
}

private sealed class Value : Comparable<Value> {
  override fun compareTo(other: Value): Int =
    if (this is IntValue && other is IntValue) {
      when {
        this.value < other.value -> -1
        this.value > other.value -> 1
        else -> 0
      }
    } else if (this is ListValue && other is ListValue) {
      val maxLength = maxOf(this.values.size, other.values.size)
      (0 until maxLength).forEach { index ->
        val leftValue = this.values.getOrNull(index)
        val rightValue = other.values.getOrNull(index)
        if (leftValue != null && rightValue != null) {
          val comp = leftValue.compareTo(rightValue)
          if (comp != 0) return comp
        } else {
          return if (leftValue == null && rightValue != null) {
            -1
          } else {
            1
          }
        }
      }
      0
    } else if (this is ListValue && other is IntValue) {
      this.compareTo(ListValue(listOf(other)))
    } else {
      ListValue(listOf(this)).compareTo(other)
    }
}

private class IntValue(val value: Int) : Value() {
  constructor(string: String) : this(string.toInt())

  override fun toString(): String = value.toString()
}

private class ListValue(val values: List<Value>) : Value() {
  constructor(string: String) : this(
    if (string == "[]") {
      emptyList<Value>()
    } else {
      val values = mutableListOf<Value>()
      val currentString = StringBuilder()
      var openBrackets = 0
      string.substring(1, string.lastIndex).forEach { char ->
        when (char) {
          '[' -> {
            currentString.append(char)
            openBrackets++
          }
          ']' -> {
            currentString.append(char)
            openBrackets--
            if (openBrackets == 0) {
              values.add(ListValue(currentString.toString()))
              currentString.clear()
            }
          }
          ',' -> {
            if (openBrackets > 0) {
              currentString.append(char)
            } else if (currentString.isNotEmpty()) {
              values.add(IntValue(currentString.toString()))
              currentString.clear()
            }
          }
          else -> {
            currentString.append(char)
          }
        }
      }
      if (currentString.isNotEmpty()) {
        values.add(IntValue(currentString.toString()))
      }
      values
    }
  )

  override fun equals(other: Any?): Boolean =
    if (other is ListValue) {
      this.toString() == other.toString()
    } else {
      super.equals(other)
    }

  override fun toString(): String = "[${values.joinToString { it.toString() }}]"
}