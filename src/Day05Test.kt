import java.io.File

fun main() {
  val testInput = File("src/Day05_test.txt").readText()
  val realInput = File("src/Day05.txt").readText()

  val part1TestOutput = topCrates(testInput, reverse = false)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == "CMZ")

  val part1RealOutput = topCrates(realInput, reverse = false)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = topCrates(testInput, reverse = true)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == "MCD")

  val part2RealOutput = topCrates(realInput, reverse = true)
  println("Part 2 Real Output: $part2RealOutput")
}