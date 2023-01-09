import java.io.File

fun main() {
  val testInput = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"""
  val realInput = File("src/Day12.txt").readText()

  val part1TestOutput = fewestSteps(testInput) { it == 'S' }
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 31)

  val part1RealOutput = fewestSteps(realInput) { it == 'S' }
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = fewestSteps(testInput) { it == 'S' || it == 'a' }
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 29)

  val part2RealOutput = fewestSteps(realInput) { it == 'S' || it == 'a' }
  println("Part 2 Real Output: $part2RealOutput")
}

/**
 * The heightmap shows the local area from above broken into a grid.
 *   a is the lowest elevation
 *   z is the highest elevation
 *   S is the starting position at elevation a
 *   E is the ending position at elevation z
 * During each step, you can move exactly one square up, down, left, or right.
 * The elevation of the destination can be at most one higher than the current elevation.
 */
fun fewestSteps(input: String, isStartPos: (Char) -> Boolean): Int {
  val hill = parseHill(input, isStartPos)

  // Breadth First Search

  val visited = hill.elevations.entries.associate { it.key to false }.toMutableMap()
  val distances: MutableMap<Pos, Int?> = hill.elevations.entries.associate { it.key to null }.toMutableMap()
  val queue = mutableListOf<Pos>()

  hill.startPosList.forEach { startPos ->
    visited[startPos] = true
    distances[startPos] = 0
    queue.add(startPos)
  }

  while (queue.isNotEmpty()) {
    val pos = queue.removeFirst()
    if (pos == hill.endPos) break

    val elevation = hill.elevations[pos]!!
    val distance = distances[pos]!!

    listOf(
      Pos(pos.x, pos.y - 1),
      Pos(pos.x, pos.y + 1),
      Pos(pos.x - 1, pos.y),
      Pos(pos.x + 1, pos.y)
    ).filter { nextPos ->
      0 <= nextPos.x && nextPos.x <= hill.maxPos.x &&
          0 <= nextPos.y && nextPos.y <= hill.maxPos.y &&
          hill.elevations[nextPos]!! <= elevation + 1 &&
          visited[nextPos] == false
    }.map { nextPos ->
      visited[nextPos] = true
      distances[nextPos] = distance + 1
      queue.add(nextPos)
    }
  }

  return distances[hill.endPos]!!
}

private fun parseHill(input: String, isStartPos: (Char) -> Boolean): Hill {
  val startPosList = mutableListOf<Pos>()
  var endPos = Pos(-1, -1)
  val elevations = mutableMapOf<Pos, Int>()
  input.lines().mapIndexed { i, line ->
    line.mapIndexed { j, char ->
      if (isStartPos(char)) startPosList.add(Pos(i, j))
      if (char == 'E') endPos = Pos(i, j)
      elevations[Pos(i, j)] = when (char) {
        'S' -> 0
        'E' -> 'z'.code - 'a'.code
        else -> char.code - 'a'.code
      }
    }
  }
  return Hill(elevations, startPosList, endPos, elevations.keys.last())
}

private data class Hill(val elevations: Map<Pos, Int>, val startPosList: List<Pos>, val endPos: Pos, val maxPos: Pos)

private data class Pos(val x: Int, val y: Int)
