data class Point(val x: Int, val y: Int) {
  operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)

  override fun toString(): String {
    return "($x,$y)"
  }
}