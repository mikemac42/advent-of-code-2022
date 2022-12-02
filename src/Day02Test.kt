import java.io.File

fun main() {
  val testInput = File("src/Day02_test.txt").readText()
  val realInput = File("src/Day02.txt").readText()

  val part1TestScore = part1Score(testInput)
  println("Part 1 Test Score: $part1TestScore")
  check(part1TestScore == 15)

  val part1RealScore = part1Score(realInput)
  println("Part 1 Real Score: $part1RealScore")

  val part2TestScore = part2Score(testInput)
  println("Part 2 Test Score: $part2TestScore")
  check(part2TestScore == 12)

  val part2RealScore = part2Score(realInput)
  println("Part 2 Real Score: $part2RealScore")
}