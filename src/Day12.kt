import java.io.File

fun main() {
  val testInput = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"""
  val realInput = File("src/Day12.txt").readText()

  val part1TestOutput = fewestSteps(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 31)

  val part1RealOutput = fewestSteps(realInput)
  println("Part 1 Real Output: $part1RealOutput")
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
fun fewestSteps(input: String): Int {
  // Parse grid
  var startPos = Pos(-1, -1)
  var endPos = Pos(-1, -1)
  val elevations = mutableMapOf<Pos, Int>()
  input.lines().mapIndexed { i, line ->
    line.mapIndexed { j, char ->
      if (char == 'S') startPos = Pos(i, j)
      if (char == 'E') endPos = Pos(i, j)
      elevations[Pos(i, j)] = when (char) {
        'S' -> 0
        'E' -> 'z'.code - 'a'.code
        else -> char.code - 'a'.code
      }
    }
  }
  val lastPos = elevations.keys.last()

  // Breadth First Search
  val visited = elevations.entries.associate { it.key to false }.toMutableMap()
  val distances: MutableMap<Pos, Int?> = elevations.entries.associate { it.key to null }.toMutableMap()
  val queue = mutableListOf<Pos>()

  visited[startPos] = true
  distances[startPos] = 0
  queue.add(startPos)

  var numLoops = 0L
  while (queue.isNotEmpty()) {
    numLoops++
    val pos = queue.removeFirst()
    if (pos == endPos) break

    val elevation = elevations[pos]!!
    val distance = distances[pos]!!

    listOf(
      Pos(pos.x, pos.y - 1),
      Pos(pos.x, pos.y + 1),
      Pos(pos.x - 1, pos.y),
      Pos(pos.x + 1, pos.y)
    ).filter { nextPos ->
      0 <= nextPos.x && nextPos.x <= lastPos.x &&
          0 <= nextPos.y && nextPos.y <= lastPos.y &&
          elevations[nextPos]!! <= elevation + 1 &&
          visited[nextPos] == false
    }.map { nextPos ->
      visited[nextPos] = true
      distances[nextPos] = distance + 1
      queue.add(nextPos)
    }
  }

  println("numLoops = $numLoops")
  return distances[endPos]!!
}

data class Pos(val x: Int, val y: Int)
