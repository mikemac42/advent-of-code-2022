import java.io.File

fun main() {
  val testInput = File("src/Day01_test.txt").readText()
  val realInput = File("src/Day01.txt").readText()

  check(part1MostCalories(testInput) == 24000)
  println(part1MostCalories(realInput))

  check(part2MostCalories(testInput) == 45000)
  println(part2MostCalories(realInput))
}