import java.io.File

fun main() {
  val testInput = File("src/Day04_test.txt").readText()
  val realInput = File("src/Day04.txt").readText()

  val part1TestOutput = countFullyContainedPairs(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 2)

  val part1RealOutput = countFullyContainedPairs(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = countOverlappingPairs(testInput)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 4)

  val part2RealOutput = countOverlappingPairs(realInput)
  println("Part 2 Real Output: $part2RealOutput")
}