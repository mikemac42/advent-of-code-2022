/**
 * Each rucksack has 2 compartments with same number of items.  Each line represents all items in a rucksack.
 * Elf that did the packing failed to follow this rule for exactly one item type per rucksack.
 * Every item type is identified by a single lowercase or uppercase letter.
 * Lowercase item types a through z have priorities 1 through 26.
 * Uppercase item types A through Z have priorities 27 through 52.
 */
fun sumPrioritiesPart1(input: String): Int =
  input.lines().sumOf { it.toRucksack().commonItem().priority() }

/**
 * Each group of 3 lines is an Elf group.
 * The badge is the item type carried by all three Elves in a group.
 */
fun sumPrioritiesPart2(input: String): Int =
  input.lines().map { it.toList() }.chunked(3).sumOf { commonItem(it).priority() }

data class Rucksack(val leftCompartment: List<Char>, val rightCompartment: List<Char>) {
  fun commonItem(): Char = commonItem(listOf(leftCompartment, rightCompartment))
}

fun String.toRucksack(): Rucksack =
  Rucksack(
    subSequence(0, length / 2).toList(),
    subSequence(length / 2, length).toList()
  )

fun commonItem(charLists: List<List<Char>>): Char {
  val searchList = charLists.first()
  val otherLists = charLists.subList(1, charLists.size)
  searchList.forEach { item ->
    if (otherLists.all { it.contains(item) }) {
      return item
    }
  }
  throw IllegalStateException()
}

fun Char.priority(): Int =
  if (this.isLowerCase()) {
    this.code - 96
  } else {
    this.code - 38
  }



