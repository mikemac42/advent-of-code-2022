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

  val part1TestOutput = levelOfMonkeyBusiness(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 10605)

  val part1RealOutput = levelOfMonkeyBusiness(realInput)
  println("Part 1 Real Output: $part1RealOutput")

//  val part2TestOutput = draw(testInput)
//  println("Part 2 Test Output:\n$part2TestOutput")
//  check(part2TestOutput == testOutput)
//
//  val part2RealOutput = draw(realInput)
//  println("Part 2 Real Output:\n$part2RealOutput")
}

data class Monkey(
  var inspectionCount: Int,
  val items: MutableList<Long>,
  val operation: (Long) -> Long,
  val test: (Long) -> Boolean,
  val nextMonkeyIfTrue: Int,
  val nextMonkeyIfFalse: Int
)

/**
 * Returns the product of the number of inspections of the 2 most active monkeys
 * after 20 rounds of keep away.
 */
fun levelOfMonkeyBusiness(input: String): Int {
  val monkeys = makeMonkeys(input)
  playRound(monkeys)
  val mostActiveMonkeys = monkeys.sortedBy { it.inspectionCount }.take(2)
  return mostActiveMonkeys[0].inspectionCount * mostActiveMonkeys[1].inspectionCount
}

fun playRound(monkeys: List<Monkey>) {
  monkeys.forEach { monkey ->
    while (monkey.items.isNotEmpty()) {
      monkey.inspectionCount++
      val item = monkey.operation(monkey.items.removeFirst()) / 3
      val nextMonkey = if (monkey.test(item)) monkey.nextMonkeyIfTrue else monkey.nextMonkeyIfFalse
      monkeys[nextMonkey].items.add(item)
    }
  }
  monkeys.forEach { println("inspections: ${it.inspectionCount} items: ${it.items}") }
}

fun makeMonkeys(input: String): List<Monkey> =
  input.split("\n\n").map { section ->
    val lines = section.lines()
    Monkey(
      inspectionCount = 0,
      items = lines[1].drop(18).split(", ").map { it.toLong() }.toMutableList(),
      operation = lines[2].drop(19).toOperation(),
      test = { x -> x % lines[3].drop(21).toLong() == 0L },
      nextMonkeyIfTrue = lines[4].drop(29).toInt(),
      nextMonkeyIfFalse = lines[5].drop(30).toInt()
    )
  }

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