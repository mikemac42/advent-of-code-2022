import java.io.File
import java.math.BigInteger

fun main() {
  // Unit Tests
  assert(6500.toBigInteger().isDivisbleBy(13))
  assert(!6501.toBigInteger().isDivisbleBy(13))
  assert(986.toBigInteger().isDivisbleBy(17))
  assert(!876.toBigInteger().isDivisbleBy(17))
  assert(475.toBigInteger().isDivisbleBy(19))
  assert(!575.toBigInteger().isDivisbleBy(19))
  assert(575.toBigInteger().isDivisbleBy(23))
  assert(!576.toBigInteger().isDivisbleBy(23))

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

//  val part1TestOutput = levelOfMonkeyBusiness(testInput, numRounds = 20, worryDivisor = 3)
//  println("Part 1 Test Output: $part1TestOutput")
//  check(part1TestOutput == BigInteger.valueOf(10605L))
//
//  val part1RealOutput = levelOfMonkeyBusiness(realInput, numRounds = 20, worryDivisor = 3)
//  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = levelOfMonkeyBusiness(testInput, numRounds = 1000)
  println("Part 2 Test Output:\n$part2TestOutput")
  check(part2TestOutput == BigInteger.valueOf(2713310158))

//  val part2RealOutput = draw(realInput)
//  println("Part 2 Real Output:\n$part2RealOutput")
}

/**
 * Returns the product of the number of inspections of the 2 most active monkeys
 * after a specified number of rounds of keep away.
 */
fun levelOfMonkeyBusiness(input: String, numRounds: Int, worryDivisor: Int? = null): BigInteger {
  val bigWorryDivisor = worryDivisor?.toBigInteger()
  val monkeys = makeMonkeys(input)
  repeat(numRounds) { round ->
    println("Playing round ${round + 1}")
    playRound(monkeys, bigWorryDivisor)
//    println("\nAfter round ${round + 1}:")
//    monkeys.forEachIndexed { index, monkey ->
//      println("Monkey $index: ${monkey.items.map { it.worryLevel }.joinToString(", ")}")
//    }
//    monkeys.forEachIndexed { index, monkey -> println("Monkey $index inspected items ${monkey.inspectionCount} times.") }
  }
  println("\nAfter round $numRounds:")
  monkeys.forEachIndexed { index, monkey -> println("Monkey $index inspected items ${monkey.inspectionCount} times.") }

  val inspectionCounts = monkeys.map { it.inspectionCount }.toMutableList()
  inspectionCounts.sort()
  val highestCounts = inspectionCounts.takeLast(2)
  return highestCounts[0].times(highestCounts[1])
}

fun makeMonkeys(input: String): List<Monkey> {
  val divisors = input.lines().mapNotNull { line ->
    if (line.contains("Test: divisible by")) line.drop(21).toInt() else null
  }
  return input.split("\n\n").map { section ->
    val lines = section.lines()
    val testDivisor = lines[3].drop(21).toInt()
    Monkey(
      inspectionCount = BigInteger.valueOf(0),
      items = lines[1].drop(18).split(", ").map { Item(it.toInt(), divisors) }.toMutableList(),
      operation = lines[2].drop(19).toOperation(),
      test = { x -> x.isWorryDivisbleBy(testDivisor) },
      nextMonkeyIfTrue = lines[4].drop(29).toInt(),
      nextMonkeyIfFalse = lines[5].drop(30).toInt()
    )
  }
}

fun String.toOperation(): (Item) -> Unit = when {
  this == "old * old" -> { x: Item -> x.square() }
  this.startsWith("old * ") -> { x: Item -> x.times(this.drop(6).toInt()) }
  this.startsWith("old + ") -> { x: Item -> x.add(this.drop(6).toInt()) }
  else -> throw IllegalArgumentException(this)
}

fun playRound(monkeys: List<Monkey>, worryDivisor: BigInteger?) {
  monkeys.forEach { monkey ->
    while (monkey.items.isNotEmpty()) {
      monkey.inspectionCount = monkey.inspectionCount.inc()
      val item = monkey.items.removeFirst()
      monkey.operation(item)
      worryDivisor?.let { item.divideBy(worryDivisor) }
      val nextMonkey = if (monkey.test(item)) monkey.nextMonkeyIfTrue else monkey.nextMonkeyIfFalse
      monkeys[nextMonkey].items.add(item)
    }
  }
}

data class Monkey(
  var inspectionCount: BigInteger,
  val items: MutableList<Item>,
  val operation: (Item) -> Unit,
  val test: (Item) -> Boolean,
  val nextMonkeyIfTrue: Int,
  val nextMonkeyIfFalse: Int
)

data class Item(var worryLevel: BigInteger, val divisibilityMap: MutableMap<Int, Boolean?>) {
  constructor(worryLevel: Int, divisors: List<Int>) : this(
    worryLevel.toBigInteger(),
    divisors.associateWith { (worryLevel % it == 0) }.toMutableMap()
  )

  fun isWorryDivisbleBy(divisor: Int): Boolean {
    val cachedResult = divisibilityMap[divisor]
    return if (cachedResult != null) {
      cachedResult
    } else {
      val result = worryLevel.isDivisbleBy(divisor)
      divisibilityMap[divisor] = result
      result
    }
  }

  fun add(amount: Int) {
    worryLevel = worryLevel.plus(amount.toBigInteger())
    divisibilityMap.replaceAll { divisor, isDivisible ->
      when (isDivisible) {
        true -> amount.isDivisibleBy(divisor)
        false -> if (amount.isDivisibleBy(divisor)) false else null
        null -> null
      }
    }
  }

  fun times(amount: Int) {
    worryLevel = worryLevel.times(amount.toBigInteger())
  }

  fun square() {
    worryLevel = worryLevel.pow(2)
  }

  fun divideBy(amount: BigInteger) {
    worryLevel = worryLevel.div(amount)
    divisibilityMap.replaceAll { _, _ -> null }
  }
}

val intLimitAsBigInt = Int.MAX_VALUE.toBigInteger()

tailrec fun BigInteger.isDivisbleBy(divisor: Int): Boolean =
  if (this < intLimitAsBigInt) {
    this.toInt().isDivisibleBy(divisor)
  } else {
    println("Determining if $this is divisible by $divisor")
    when (divisor) {
      2 -> !this.testBit(0)
      3 -> this.toString().sumOf { it.digitToInt() } % 3 == 0
      5 -> {
        val lastDigit = this.toString().last()
        lastDigit == '0' || lastDigit == '5'
      }
      13 -> {
        val string = this.toString()
        val last = string.last().digitToInt()
        val rest = string.take(string.length - 1).toBigInteger()
        ((last * 4).toBigInteger() + rest).isDivisbleBy(divisor)
      }
      17 -> {
        val string = this.toString()
        val last = string.last().digitToInt()
        val rest = string.take(string.length - 1).toBigInteger()
        ((last * 5).toBigInteger() + rest).isDivisbleBy(divisor)
      }
      19 -> {
        val string = this.toString()
        val last = string.last().digitToInt()
        val rest = string.take(string.length - 1).toBigInteger()
        ((last * 2).toBigInteger() + rest).isDivisbleBy(divisor)
      }
      23 -> {
        val string = this.toString()
        val last = string.last().digitToInt()
        val rest = string.take(string.length - 1).toBigInteger()
        ((last * 7).toBigInteger() + rest).isDivisbleBy(divisor)
      }
      else -> {
        throw IllegalArgumentException()
      }
    }
  }

fun Int.isDivisibleBy(divisor: Int): Boolean =
  this % divisor == 0