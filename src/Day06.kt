import java.io.File

fun main() {
  val testInput = """mjqjpqmgbljsphdztnvjfqwrcgsmlb"""
  val realInput = File("src/Day06.txt").readText()

  val part1TestOutput = endOfMarker(testInput, 4)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 7)
  println("Part 1 Real Output: ${endOfMarker(realInput, 4)}")

  val part2TestOutput = endOfMarker(testInput, 14)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 19)
  println("Part 2 Real Output: ${endOfMarker(realInput, 14)}")
}

fun endOfMarker(input: String, markerSize: Int): Int =
  input.windowed(markerSize).indexOfFirst { it.toSet().size == markerSize } + markerSize
