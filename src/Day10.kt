import java.io.File

fun main() {
  val testInput = """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop"""
  val testOutput = """##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######....."""
  val realInput = File("src/Day10.txt").readText()

  val part1TestOutput = sumSignalStrengths(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 13140)

  val part1RealOutput = sumSignalStrengths(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = draw(testInput)
  println("Part 2 Test Output:\n$part2TestOutput")
  check(part2TestOutput == testOutput)

  val part2RealOutput = draw(realInput)
  println("Part 2 Real Output:\n$part2RealOutput")
}

/**
 * The CPU has a single register, X, which starts with the value 1.
 * "addx V" takes 2 cycles and X register is increased by the value V (which can be negative).
 * "noop" takes 1 cycle and does nothing.
 * Signal strength is the cycle number multiplied by the value of the X register.
 * Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
 * What is the sum of these six signal strengths?
 */
fun sumSignalStrengths(input: String): Int {
  val registerValues = registerValues(input)
  val cyclesToAdd = IntProgression.fromClosedRange(20, registerValues.size, 40)
  val signalStrengths = cyclesToAdd.map { it * registerValues[it - 1] }
  return signalStrengths.sum()
}

fun registerValues(input: String): List<Int> {
  val signalStrengths = mutableListOf<Int>()
  var clock = 1
  var x = 1
  input.lines().forEach { line ->
    signalStrengths.add(x)
    clock++
    if (line != "noop") {
      signalStrengths.add(x)
      clock++
      x += line.substring(5).toInt()
    }
  }
  return signalStrengths
}

/**
 * The sprite is 3 pixels wide, and the X register sets the horizontal position of the middle.
 * CRT is 40px wide and 6px high.
 * The CRT draws the top row of pixels left-to-right, then the row below that, etc.
 * The left-most pixel is position 0, and the right-most pixel is position 39.
 * CRT draws a single pixel during each cycle.
 * If 1 of its 3 pixels is the pixel currently being drawn then it is a lit pixel (#);
 * otherwise, the screen leaves the pixel dark (.)
 */
fun draw(input: String): String {
  val registerValues = registerValues(input)
  return registerValues.mapIndexed { index, x ->
    val newLine = if (index > 0 && index % 40 == 0) "\n" else ""
    val xPos = index % 40
    val pixel = if (xPos - 2 < x && x < xPos + 2) {
      "#"
    } else {
      "."
    }
    newLine + pixel
  }.joinToString("")
}