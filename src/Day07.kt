import java.io.File

fun main() {
  val testInput = """${'$'} cd /
${'$'} ls
dir a
14848514 b.txt
8504156 c.dat
dir d
${'$'} cd a
${'$'} ls
dir e
29116 f
2557 g
62596 h.lst
${'$'} cd e
${'$'} ls
584 i
${'$'} cd ..
${'$'} cd ..
${'$'} cd d
${'$'} ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k"""
  val testInput2 = """${'$'} cd /
${'$'} ls
dir a
dir b
${'$'} cd a
${'$'} ls
1 some.txt
${'$'} cd ..
${'$'} cd b
${'$'} ls
dir a
${'$'} cd a
${'$'} ls
1 other.txt"""
  val realInput = File("src/Day07.txt").readText()

  val part1TestOutput = sumDirectorySizes(testInput)
  println("Part 1 Test Output: $part1TestOutput")
  check(part1TestOutput == 95437L)

  val part1Test2Output = sumDirectorySizes(testInput2)
  println("Part 1 Test2 Output: $part1Test2Output")
  check(part1Test2Output == 5L)

  val part1RealOutput = sumDirectorySizes(realInput)
  println("Part 1 Real Output: $part1RealOutput")

  val part2TestOutput = smallestDirToDelete(testInput)
  println("Part 2 Test Output: $part2TestOutput")
  check(part2TestOutput == 24933642L)

  println("Part 2 Real Output: ${smallestDirToDelete(realInput)}")
}

data class Dir(
  val name: String,
  val path: String,
  var parentDir: Dir?,
  val childDirs: MutableList<Dir>,
  val fileSizes: MutableList<Long>
) {
  fun size(): Long = fileSizes.sum() + childDirs.sumOf { it.size() }
}

/**
 * Find all of the directories with a total size of at most 100000.
 * What is the sum of the total sizes of those directories?
 * This process can count files more than once!
 */
fun sumDirectorySizes(input: String): Long =
  buildDirPathToSizeMap(input).values.filter { it <= 100_000L }.sum()

fun smallestDirToDelete(input: String): Long {
  val dirPathToSizeMap = buildDirPathToSizeMap(input)
  val freeSpace = 70_000_000L - dirPathToSizeMap["/"]!!
  val minSpaceToDelete = 30_000_000L - freeSpace
  return dirPathToSizeMap.values.sorted().first { it >= minSpaceToDelete }
}

fun buildDirPathToSizeMap(input: String): Map<String, Long> {
  val lines = input.lines().filterNot { it.startsWith("$ ls") }
  val rootDir = Dir("/", "/", null, mutableListOf(), mutableListOf())
  val dirPathMap = mutableMapOf(rootDir.path to rootDir)
  var currentDir = rootDir
  lines.forEach { line ->
    if (line == "$ cd /") {
      currentDir = rootDir
    } else if (line == "$ cd ..") {
      currentDir = currentDir.parentDir!!
    } else if (line.startsWith("$ cd ")) {
      currentDir = currentDir.childDirs.find { it.name == line.substring(5) }!!
    } else if (line.startsWith("dir ")) {
      val name = line.substring(4)
      val newDir = Dir(name, "${currentDir.path}$name/", parentDir = currentDir, mutableListOf(), mutableListOf())
      dirPathMap[newDir.path] = newDir
      currentDir.childDirs.add(newDir)
    } else {
      currentDir.fileSizes.add(line.split(' ').first().toLong())
    }
  }
  return dirPathMap.entries.associate { (path, dir) -> path to dir.size() }
}