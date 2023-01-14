import java.io.File

fun main() {
  val testInput = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9"""
  val realInput = File("src/Day14.txt").readText()

  val part1TestOutput = unitsOfSandToAbyss(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 24)

  val part1RealOutput = unitsOfSandToAbyss(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = unitsOfSandToAbyss(testInput, useFloor = true)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 93)

  val part2RealOutput = unitsOfSandToAbyss(realInput, useFloor = true)
  println("Part 2 Real Output: $part2RealOutput")
}

fun unitsOfSandToAbyss(input: String, useFloor: Boolean = false): Int {
  val grid = makeGrid(input, useFloor)
  var sandCount = 0
  while (grid.addSand() != null) {
    sandCount++
  }
  println(grid.toString())
  return sandCount
}

private fun makeGrid(input: String, useFloor: Boolean): Grid {
  val structures = input.lines().map { line ->
    line.split(" -> ").map { coordinates ->
      val numbers = coordinates.split(',').map { it.toInt() }
      Point(numbers[0], numbers[1])
    }
  }.toMutableList()
  if (useFloor) {
    val points = structures.flatten()
    val maxY = points.maxOf { it.y } + 2
    val entryPoint = Point(500, 0)
    val floor = listOf(Point(entryPoint.x - maxY - 1, maxY), Point(entryPoint.x + maxY + 1, maxY))
    structures.add(floor)
  }
  return structures.toGrid()
}

private fun List<Structure>.toGrid(): Grid {
  val entryPoint = Point(500, 0)
  val points = this.flatten() + entryPoint
  val minPoint = Point(points.minOf { it.x }, points.minOf { it.y })
  val maxPoint = Point(points.maxOf { it.x }, points.maxOf { it.y })
  val materials = Array(maxPoint.x - minPoint.x + 1) { x ->
    Array(maxPoint.y - minPoint.y + 1) { y ->
      if (x == entryPoint.x - minPoint.x && y == entryPoint.y - minPoint.y) {
        Material.ENTRY
      } else {
        Material.AIR
      }
    }
  }
  this.forEach { structure ->
    structure.forEachIndexed { index, point ->
      if (index > 0) {
        val start = structure[index - 1] - minPoint
        val end = point - minPoint
        if (start.x == end.x) {
          (minOf(start.y, end.y)..maxOf(start.y, end.y)).forEach { y ->
            materials[start.x][y] = Material.ROCK
          }
        } else {
          (minOf(start.x, end.x)..maxOf(start.x, end.x)).forEach { x ->
            materials[x][start.y] = Material.ROCK
          }
        }
      }
    }
  }
  return Grid(materials, entryPoint - minPoint)
}

private data class Grid(val materials: Array<Array<Material>>, val entryPoint: Point) {
  fun addSand(): Point? {
    if (materials[entryPoint.x][entryPoint.y] == Material.SAND) return null
    var sand = entryPoint
    while (true) {
      sand = if (sand.y == materials[0].lastIndex) {
        return null
      } else if (materials[sand.x][sand.y + 1] == Material.AIR) {
        Point(sand.x, sand.y + 1)
      } else if (sand.x == 0) {
        return null
      } else if (materials[sand.x - 1][sand.y + 1] == Material.AIR) {
        Point(sand.x - 1, sand.y + 1)
      } else if (materials[sand.x + 1][sand.y + 1] == Material.AIR) {
        Point(sand.x + 1, sand.y + 1)
      } else {
        break
      }
    }
    materials[sand.x][sand.y] = Material.SAND
    return sand
  }

  override fun toString(): String {
    val stringBuilder = StringBuilder()
    materials.first().indices.forEach { y ->
      materials.indices.forEach { x ->
        stringBuilder.append(materials[x][y].char)
      }
      stringBuilder.appendLine()
    }
    return stringBuilder.toString()
  }
}

private typealias Structure = List<Point>

private enum class Material(val char: Char) {
  AIR('.'), ROCK('#'), SAND('o'), ENTRY('+')
}