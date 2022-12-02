fun part1MostCalories(input: String): Int {
  var currentFood = 0
  var maxFood = 0
  input.lines().forEach { line ->
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

fun part2MostCalories(input: String): Int {
  var currentFood = 0
  val topElves = mutableListOf<Int>()
  input.lines().forEach { line ->
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
