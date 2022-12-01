fun main() {
  fun part1(input: List<String>): Int {
    var currentFood = 0
    var maxFood = 0

    input.forEach { line ->
      if (line.isBlank()) {
        if (currentFood > maxFood) {
          maxFood = currentFood
        }

        currentFood = 0
      } else {
        currentFood += line.toInt()
      }
    }

    if (currentFood > maxFood) {
      maxFood = currentFood
    }

    return maxFood
  }

  fun part2(input: List<String>): Int {
    var currentFood = 0
    val topElves = mutableListOf<Int>()

    input.forEach { line ->
      if (line.isBlank()) {
        topElves.add(currentFood)
        if (topElves.size == 4) {
          topElves.remove(topElves.min())
        }

        currentFood = 0
      } else {
        currentFood += line.toInt()
      }
    }

    topElves.add(currentFood)
    if (topElves.size == 4) {
      topElves.remove(topElves.min())
    }

    return topElves.sum()
  }

  val testInput = readInput("Day01_test")
  val input = readInput("Day01")

  check(part1(testInput) == 24000)
  println(part1(input))

  check(part2(testInput) == 45000)
  println(part2(input))
}
