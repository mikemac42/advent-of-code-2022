import java.io.File

fun main() {
  val testInput = File("src/Day03_test.txt").readText()
  val realInput = File("src/Day03.txt").readText()

  val part1TestOutput = sumPrioritiesPart1(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 157)

  val part1RealOutput = sumPrioritiesPart1(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = sumPrioritiesPart2(testInput)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 70)

  val part2RealOutput = sumPrioritiesPart2(realInput)
  println("Part 2 Real Output: $part2RealOutput")
}