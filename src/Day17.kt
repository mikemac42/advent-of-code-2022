import java.io.File

fun main() {
  val testInput = """>>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"""
  val realInput = File("src/Day17.txt").readText()

  val part1TestOutput = towerHeight(testInput, 2022)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 3068)

  val part1RealOutput = towerHeight(realInput, 2022)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = bigTowerHeight(testInput, 1000000000000L)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 1514285714288L)

  val part2RealOutput = bigTowerHeight(realInput, 1000000000000L)
  println("Part 2 Real Output: $part2RealOutput")
}

private const val chamberWidth = 7
private const val leftChar = '<'
private const val rightChar = '>'
private const val emptyChar = ' '
private const val rockChar = '#'

// This is not working
private fun bigTowerHeight(jets: String, numRocks: Long): Long {
  val numRepeatingRocks = jets.length * Shape.values().size * 1000
  val numRepeats = numRocks / numRepeatingRocks
  val numRemainingRocks = numRocks % numRepeatingRocks
  val repeatingHeight = towerHeight(jets, numRepeatingRocks).toLong()
  val remainderHeight = towerHeight(jets, numRemainingRocks.toInt()).toLong()
  return numRepeats * repeatingHeight + remainderHeight
}

private fun towerHeight(jets: String, numRocks: Int): Int {
  val levels = mutableListOf(emptyLevel(), emptyLevel(), emptyLevel()) // level 0 is the bottom
  var jetIndex = 0
  var height = 0

  repeat(numRocks) { rockIndex ->
    val shape = Shape.values()[rockIndex % 5]
    var shapePos = Point(2, height + 3) // position of bottom left bounds of shape

    while (levels.size < shapePos.y + shape.height) {
      levels.add(emptyLevel())
    }

    var falling = true
    while (falling) {
      // jet move
      val jet = jets[jetIndex]
      jetIndex = (jetIndex + 1) % jets.length
      when (jet) {
        leftChar -> {
          if (shapePos.x - 1 >= 0 &&
              shape.points.none { levels[shapePos.y + it.y][shapePos.x - 1 + it.x] == rockChar }) {
            shapePos = Point(shapePos.x - 1, shapePos.y)
          }
        }
        rightChar -> {
          if (shapePos.x + 1 + shape.width <= chamberWidth &&
              shape.points.none { levels[shapePos.y + it.y][shapePos.x + 1 + it.x] == rockChar }) {
            shapePos = Point(shapePos.x + 1, shapePos.y)
          }
        }
        else -> throw IllegalStateException("illegal jet char")
      }
      // gravity move or stop
      if (shapePos.y - 1 >= 0 &&
          shape.points.none { levels[shapePos.y - 1 + it.y][shapePos.x + it.x] == rockChar }) {
        shapePos = Point(shapePos.x, shapePos.y - 1)
      } else {
        shape.points.forEach {
          levels[shapePos.y + it.y][shapePos.x + it.x] = rockChar
        }
        falling = false
      }
    }

    height = maxOf(shapePos.y + shape.height, height)
  }

//  printLevels(numRocks, height, levels)
  return height
}

private fun printLevels(rockIndex: Int, height: Int, levels: MutableList<CharArray>) {
  println("After rock $rockIndex the height is $height:")
  levels.reversed().forEach { level -> println("|${level.concatToString()}|") }
  println("+-------+\n")
}

private fun emptyLevel() = CharArray(chamberWidth) { emptyChar }

/**
 * Grid dimensions increase left to right and bottom to top
 */
private enum class Shape(val width: Int, val height: Int, val points: List<Point>) {
  HorizontalLine(4, 1, listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))),
  Plus(3, 3, listOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2))),
  Corner(3, 3, listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2))),
  VerticalLine(1, 4, listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))),
  Square(2, 2, listOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1)))
}
