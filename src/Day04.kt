/**
 * Each section has a unique ID number.
 * Each Elf is assigned a range of section IDs, 2 elves per line. e.g "1-3,5-12"
 * In how many assignment pairs does one range fully contain the other?
 */
fun countFullyContainedPairs(input: String): Int =
  input.lines().count { line ->
    val numbers = line.split(',', '-').map { it.toInt() }
    (numbers[0] <= numbers[2] && numbers[3] <= numbers[1]) ||
        (numbers[2] <= numbers[0] && numbers[1] <= numbers[3])
  }

/**
 * How many pairs overlap at all?
 */
fun countOverlappingPairs(input: String): Int =
  input.lines().count { line ->
    val numbers = line.split(',', '-').map { it.toInt() }
    !(numbers[0] > numbers[3] || numbers[1] < numbers[2])
  }