import java.io.File

fun main() {
  val testInput = """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1"""
  val realInput = File("src/Day11.txt").readText()

  val part1TestOutput = levelOfMonkeyBusiness(testInput, numRounds = 20, worryDivisor = 3)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 10605L)

  val part1RealOutput = levelOfMonkeyBusiness(realInput, numRounds = 20, worryDivisor = 3)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = levelOfMonkeyBusiness(testInput, numRounds = 10000)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 2713310158L)

  val part2RealOutput = levelOfMonkeyBusiness(realInput, numRounds = 10000)
  println("Part 2 Real Output: $part2RealOutput")
}

/**
 * Returns the product of the number of inspections of the 2 most active monkeys
 * after 20 rounds of keep away.
 */
fun levelOfMonkeyBusiness(input: String, numRounds: Int, worryDivisor: Long? = null): Long {
  val monkeys = makeMonkeys(input)
  val productOfDivisors = monkeys.map { it.divisor }.reduce(Long::times)
  repeat(numRounds) {
    playRound(monkeys, worryDivisor, productOfDivisors)
//    println("== After round ${it + 1} ==")
    monkeys.forEachIndexed { index, monkey ->
//      println("Monkey $index inspected items ${monkey.inspectionCount} times.")
    }
  }
  val sortedInspectionCounts = monkeys.map { it.inspectionCount }.sortedDescending()
  return sortedInspectionCounts[0] * sortedInspectionCounts[1]
}

fun makeMonkeys(input: String): List<Monkey> =
  input.split("\n\n").map { section ->
    val lines = section.lines()
    Monkey(
      inspectionCount = 0,
      items = lines[1].drop(18).split(", ").map { it.toLong() }.toMutableList(),
      operation = lines[2].drop(19).toOperation(),
      divisor = lines[3].drop(21).toLong(),
      nextMonkeyIfTrue = lines[4].drop(29).toInt(),
      nextMonkeyIfFalse = lines[5].drop(30).toInt()
    )
  }

fun playRound(monkeys: List<Monkey>, worryDivisor: Long?, productOfDivisors: Long) {
  monkeys.forEach { monkey ->
    while (monkey.items.isNotEmpty()) {
      monkey.inspectionCount++
      var item = monkey.operation(monkey.items.removeFirst())
      if (worryDivisor == null) {
        item %= productOfDivisors
      } else {
        item /= worryDivisor
      }
      val nextMonkey = if (item % monkey.divisor == 0L) monkey.nextMonkeyIfTrue else monkey.nextMonkeyIfFalse
      monkeys[nextMonkey].items.add(item)
    }
  }
}

data class Monkey(
  var inspectionCount: Long,
  val items: MutableList<Long>,
  val operation: (Long) -> Long,
  val divisor: Long,
  val nextMonkeyIfTrue: Int,
  val nextMonkeyIfFalse: Int
)

fun String.toOperation(): (Long) -> Long =
  if (this == "old * old") {
    { x: Long -> x * x }
  } else if (this.startsWith("old * ")) {
    { x: Long -> x * this.drop(6).toLong() }
  } else if (this.startsWith("old + ")) {
    { x: Long -> x + this.drop(6).toLong() }
  } else {
    throw IllegalArgumentException(this)
  }
