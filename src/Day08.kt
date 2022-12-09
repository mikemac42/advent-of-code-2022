import java.io.File

fun main() {
  val testInput = """30373
25512
65332
33549
35390"""
  val realInput = File("src/Day08.txt").readText()

  val part1TestOutput = treesVisible(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 21)

  val part1RealOutput = treesVisible(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = maxScenicScore(testInput)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 8)

  println("Part 2 Real Output: ${maxScenicScore(realInput)}")
}

/**
 * Each tree is a single digit (0-9) whose value is its height.
 * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it.
 * Only look up, down, left, or right from any given tree.
 * All of the trees around the edge of the grid are visible.
 * How many trees are visible from outside the grid?
 */
fun treesVisible(input: String): Int {
  val heightgrid = input.toGrid()
  val n = heightgrid.size
  val visibilityGrid = heightgrid.mapIndexed { j, row ->
    row.mapIndexed { i, height ->
      if (i == 0 || i == n - 1 || j == 0 || j == n - 1
        || row.subList(0, i).all { it < height } // left
        || row.subList(i + 1, n).all { it < height } // right
        || heightgrid.subList(0, j).all { it[i] < height } // up
        || heightgrid.subList(j + 1, n).all { it[i] < height } // down
      ) {
        1
      } else {
        0
      }
    }
  }
  return visibilityGrid.sumOf { it.sum() }
}

/**
 * What is the highest scenic score possible for any tree?
 */
fun maxScenicScore(input: String): Int {
  val heightGrid = input.toGrid()
  val n = heightGrid.size
  val scoreGrid = heightGrid.mapIndexed { j, row ->
    row.mapIndexed { i, height ->
      listOf(
        row.subList(0, i).reversed(), // left
        row.subList(i + 1, n), // right
        heightGrid.subList(0, j).map { it[i] }.reversed(), // up
        heightGrid.subList(j + 1, n).map { it[i] } // down
      ).map { treeRow ->
        if (treeRow.all { it < height }) {
          treeRow.size
        } else {
          treeRow.indexOfFirst { it >= height } + 1
        }
      }.reduce { acc, e ->
        acc * e
      }
    }
  }
  return scoreGrid.maxOf { it.max() }
}

fun String.toGrid(): List<List<Int>> =
  lines().map { line ->
    line.map { char ->
      char.digitToInt()
    }
  }