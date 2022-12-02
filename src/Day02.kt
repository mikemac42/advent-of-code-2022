fun String.sumCharScores(charScores: Map<Char, Int>): Int =
  sumOf { charScores[it] ?: 0 }

fun String.sumLineScores(lineScores: Map<String, Int>): Int =
  lines().sumOf { lineScores[it] ?: 0 }

/**
 * The first column is what your opponent is going to play:
 *   A for Rock, B for Paper, and C for Scissors
 * The second column, you reason, must be what you should play in response:
 *   X for Rock, Y for Paper, and Z for Scissors
 * The score for a single round is the score for the shape you selected:
 *   1 for Rock, 2 for Paper, and 3 for Scissors
 * plus the score for the outcome of the round:
 *   0 if you lost, 3 if the round was a draw, and 6 if you won
 */
fun part1Score(strategyGuide: String): Int {
  val shapeScore = strategyGuide.sumCharScores(
    mapOf(
      'X' to 1,
      'Y' to 2,
      'Z' to 3
    )
  )
  val outcomeScore = strategyGuide.sumLineScores(
    mapOf(
      "A X" to 3,
      "A Y" to 6,
      "B Y" to 3,
      "B Z" to 6,
      "C Z" to 3,
      "C X" to 6
    )
  )
  return shapeScore + outcomeScore
}

/**
 * X means lose, Y means draw, and Z means win
 */
fun part2Score(strategyGuide: String): Int {
  val shapeScore = strategyGuide.sumLineScores(
    mapOf(
      "A X" to 3,
      "A Y" to 1,
      "A Z" to 2,
      "B X" to 1,
      "B Y" to 2,
      "B Z" to 3,
      "C X" to 2,
      "C Y" to 3,
      "C Z" to 1
    )
  )
  val outcomeScore = strategyGuide.sumCharScores(
    mapOf(
      'Y' to 3,
      'Z' to 6
    )
  )
  return shapeScore + outcomeScore
}

