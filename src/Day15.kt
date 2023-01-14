import java.io.File
import kotlin.math.abs

fun main() {
  val testInput = """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3"""
  val realInput = File("src/Day15.txt").readText()

  val part1TestOutput = numNonBeaconPoints(testInput, row = 10)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 26)

  val part1RealOutput = numNonBeaconPoints(realInput, row = 2000000)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = distressBeaconFrequency(testInput, 0..20)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 56000011L)

  val part2RealOutput = distressBeaconFrequency(realInput, 0..4000000)
  println("Part 2 Real Output: $part2RealOutput")
}

/**
 * Sensors and beacons always exist at integer coordinates.
 * Sensors detect the closest beacon based on Manhattan distance.
 * How many positions in the given row that cannot contain a beacon?
 */
fun numNonBeaconPoints(input: String, row: Int): Int =
  parseSensors(input).flatMap { it.nonBeaconPoints(row) }.toSet().size

/**
 * The distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.
 * Tuning frequency is found by multiplying its x coordinate by 4000000 and then adding its y coordinate.
 * What is the tuning frequency of the distress beacon?
 */
fun distressBeaconFrequency(input: String, range: IntRange): Long {
  val sensors = parseSensors(input)

  fun pointOrNull(x: Int, y: Int): Point? =
    if (x in range && y in range) Point(x, y) else null

  val possiblePoints = sensors.flatMap { sensor ->
    sensor.possibleDistressBeaconPoints(::pointOrNull)
  }.toSet()

  possiblePoints.forEach { point ->
    if (sensors.all { it.isValidBeacon(point) }) {
      return point.x.toLong() * 4000000L + point.y.toLong()
    }
  }

  throw IllegalStateException()
}

private data class Sensor(
  val sensorPoint: Point,
  val beaconPoint: Point,
  val beaconDistance: Int = manhattanDistance(sensorPoint, beaconPoint)
) {

  fun nonBeaconPoints(row: Int? = null): List<Point> =
    (sensorPoint.x - beaconDistance..sensorPoint.x + beaconDistance).flatMap { x ->
      val possiblePoints = row?.let { listOf(Point(x, it)) }
        ?: (sensorPoint.y - beaconDistance..sensorPoint.y + beaconDistance).map { y -> Point(x, y) }

      possiblePoints.minus(beaconPoint).mapNotNull { point ->
        if (manhattanDistance(sensorPoint, point) <= beaconDistance) {
          point
        } else {
          null
        }
      }
    }

  fun possibleDistressBeaconPoints(pointOrNull: (Int, Int) -> Point?): List<Point> {
    val topRight = (sensorPoint.x..sensorPoint.x + beaconDistance).mapIndexedNotNull { index, x ->
      pointOrNull(x, sensorPoint.y - beaconDistance - 1 + index)
    }
    val rightBottom = (sensorPoint.y..sensorPoint.y + beaconDistance).mapIndexedNotNull { index, y ->
      pointOrNull(sensorPoint.x + beaconDistance + 1 - index, y)
    }
    val bottomLeft = (sensorPoint.x..sensorPoint.x - beaconDistance).mapIndexedNotNull { index, x ->
      pointOrNull(x, sensorPoint.y + beaconDistance + 1 - index)
    }
    val leftTop = (sensorPoint.y..sensorPoint.y - beaconDistance).mapIndexedNotNull { index, y ->
      pointOrNull(sensorPoint.x - beaconDistance - 1 + index, y)
    }
    return topRight + rightBottom + bottomLeft + leftTop
  }

  fun isValidBeacon(point: Point): Boolean =
    manhattanDistance(sensorPoint, point) > beaconDistance
}

private fun manhattanDistance(p1: Point, p2: Point): Int =
  abs(p1.x - p2.x) + abs(p1.y - p2.y)

private fun parseSensors(input: String): List<Sensor> {
  val regex = Regex("""Sensor at x=(-?\d*), y=(-?\d*): closest beacon is at x=(-?\d*), y=(-?\d*)""")
  return input.lines().map { line ->
    val coordinates = regex.find(line)!!.groups.drop(1).map { it!!.value.toInt() }
    Sensor(Point(coordinates[0], coordinates[1]), Point(coordinates[2], coordinates[3]))
  }
}

