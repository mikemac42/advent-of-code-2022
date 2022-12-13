import java.io.File

fun main() {
  val testInput = """R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2"""
  val testInput2 = """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"""
  val realInput = File("src/Day09.txt").readText()

  val part1TestOutput = numPositionsTailVisited(testInput, 2)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 13)

  val part1RealOutput = numPositionsTailVisited(realInput, 2)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = numPositionsTailVisited(testInput2, 10)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 36)

  println("Part 2 Real Output: ${numPositionsTailVisited(realInput, 10)}")
}

data class Knot(var x: Int, var y: Int) {
  fun follow(leader: Knot) {
    val hDiff = leader.x - x
    val vDiff = leader.y - y
    when {
      hDiff < -1 -> when {
        vDiff < 0 -> {
          x--; y--
        }
        vDiff > 0 -> {
          x--; y++
        }
        else -> x--
      }
      hDiff == -1 -> when {
        vDiff < -1 -> {
          x--;y--
        }
        vDiff > 1 -> {
          x--;y++
        }
      }
      hDiff == 0 -> when {
        vDiff < -1 -> y--
        vDiff > 1 -> y++
      }
      hDiff == 1 -> when {
        vDiff < -1 -> {
          x++; y--
        }
        vDiff > 1 -> {
          x++; y++
        }
      }
      hDiff > 1 -> when {
        vDiff < 0 -> {
          x++; y--
        }
        vDiff > 0 -> {
          x++; y++
        }
        else -> x++
      }
    }
  }
}

/**
 * Head (H) and tail (T) must always be touching (diagonally adjacent and even overlapping both count as touching).
 * If the head is ever two steps directly up, down, left, or right from the tail,
 *   the tail must also move one step in that direction.
 * If the head and tail aren't touching and aren't in the same row or column,
 *   the tail always moves one step diagonally.
 * Head and the tail both start at the same position, overlapping.
 */
fun numPositionsTailVisited(input: String, numKnots: Int): Int {
  val knots = (1..numKnots).map { Knot(0, 0) }
  val tailPositionsVisited = mutableSetOf(0 to 0)
  input.lines().flatMap { line ->
    (1..line.substring(2).toInt()).map { line[0] }
  }.forEach { dir ->
    when (dir) {
      'L' -> knots[0].x--
      'R' -> knots[0].x++
      'U' -> knots[0].y++
      else -> knots[0].y--
    }
    knots.indices.forEach { index ->
      if (index > 0) knots[index].follow(knots[index - 1])
    }
    tailPositionsVisited.add(knots.last().x to knots.last().y)
  }
  return tailPositionsVisited.size
}
