import java.io.File

import edu.princeton.cs.algs4.Picture
import org.specs2.mutable._

class SeamCarverSpec extends Specification {
  "null picture in constructor causes an exception" >> {
    new SeamCarver(null) should throwA[NullPointerException]
  }

  "repeated calls to picture() return reference to same Picture object" >> {
    val picture = new Picture(20, 30)
    val seamCarver = new SeamCarver(picture)

    seamCarver.picture() should beTheSameAs(picture)
    seamCarver.picture() should beTheSameAs(picture)
  }

  "energy checks its x-bound" >> {
    val seamCarver = new SeamCarver(new Picture(20, 30))
    seamCarver.energy(20, 0) should throwA[IndexOutOfBoundsException]
    seamCarver.energy(-1, 0) should throwA[IndexOutOfBoundsException]
  }

  "energy checks its y-bound" >> {
    val seamCarver = new SeamCarver(new Picture(20, 30))
    seamCarver.energy(0, 30) should throwA[IndexOutOfBoundsException]
    seamCarver.energy(0, -1) should throwA[IndexOutOfBoundsException]
  }

  "energy returns the correct value for a 3x4.png inside the border" >> {
    val seamCarver = newSeamCarver("3x4.png")
    seamCarver.energy(1, 2) should_=== Math.sqrt(52024.0)
  }

  "energy returns 1000 for 3x4.png at the border" >> {
    val seamCarver = newSeamCarver("3x4.png")
    seamCarver.energy(2, 3) should_=== 1000.0
  }

  "finds a vertical seam in 6x5.png" >> {
    val seamCarver = newSeamCarver("6x5.png")
    val seam = seamCarver.findVerticalSeam().toList
    seam.slice(1, 4) should_=== List(4, 3, 2)
    seam.head should beAnyOf(3, 4, 5)
    seam.last should beAnyOf(1, 2, 3)
  }

  "finds a horizontal seam in 6x5.png" >> {
    val seamCarver = newSeamCarver("6x5.png")
    val seam = seamCarver.findHorizontalSeam().toList
    seam.head should beAnyOf(1, 2, 3)
    seam.tail.dropRight(1) should_=== List(2, 1, 2, 1)
    seam.last should beAnyOf(0, 1, 2)
  }

  "finds a vertical seam in chameleon.png" >> {
    val seamCarver = newSeamCarver("chameleon.png")
    val energy = seamCarver.findVerticalSeam().toList.zipWithIndex.map{case (x, y) => seamCarver.energy(x, y)}.sum
    energy should_=== 2487.0629785467218d
  }

  "finds a vertical seam in 4x6.png" >> {
    val seamCarver = newSeamCarver("4x6.png")
    val seam = seamCarver.findVerticalSeam().toList
    val energy = seam.zipWithIndex.map{case (x, y) => seamCarver.energy(x, y)}.sum

    seam.slice(1, 5) should_=== List(2, 1, 1, 2)
    energy should_=== 2706.3701162032385d
  }

  "finds a vertical seam in 12x10.png" >> {
    val seamCarver = newSeamCarver("12x10.png")
    val seam = seamCarver.findVerticalSeam().toList
    val energy = seam.zipWithIndex.map{case (x, y) => seamCarver.energy(x, y)}.sum

    energy should_=== 3311.007347194807
  }

  "finds a vertical seam in 8x1.png" >> {
    val seamCarver = newSeamCarver("8x1.png")
    val seam = seamCarver.findVerticalSeam().toList
    val energy = seam.zipWithIndex.map{case (x, y) => seamCarver.energy(x, y)}.sum

    energy should_=== 1000.0
  }

  "finds a vertical seam in 1x1.png" >> {
    val seamCarver = newSeamCarver("1x1.png")
    val seam = seamCarver.findVerticalSeam().toList
    val energy = seam.zipWithIndex.map{case (x, y) => seamCarver.energy(x, y)}.sum

    energy should_=== 1000.0
  }

  "finds a horizontal seam in 12x10.png" >> {
    val seamCarver = newSeamCarver("12x10.png")
    val seam = seamCarver.findHorizontalSeam().toList
    val energy = seam.zipWithIndex.map{case (y, x) => seamCarver.energy(x, y)}.sum

    energy should_=== 3878.866388384648
  }

  "remove horizontal seam" >> {
    val original = newSeamCarver("3x4.png")
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](1, 1, 1))
    shrunk.width() should_== 3
    shrunk.height() should_== 3
    shrunk.picture.get(1, 1) should_=== original.picture.get(1, 2)
  }

  "remove horizontal seam with out of bounds values in the seam" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](2, 3, 4)) should throwAn[IllegalArgumentException]
    shrunk.removeHorizontalSeam(Array[Int](-2, 3, 2)) should throwAn[IllegalArgumentException]
  }

  "remove horizontal seam with jagged seam" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](0, 2, 1)) should throwAn[IllegalArgumentException]
  }

  "remove horizontal seam with array too long" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](1, 1, 1, 1)) should throwAn[IllegalArgumentException](message = "bad seam length")
  }

  "remove horizontal seam with array too shoort" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](1)) should throwAn[IllegalArgumentException](message = "bad seam length")
  }

  "remove vertical seam with array too long" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](1, 1, 1, 1, 1)) should throwAn[IllegalArgumentException](message = "bad seam length")
  }

  "remove vertical seam with array too shoort" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](1)) should throwAn[IllegalArgumentException](message = "bad seam length")
  }

  "remove vertical seam with 1x8 picture" >> {
    val shrunk = newSeamCarver("1x8.png")
    shrunk.removeVerticalSeam(Array[Int](0, 2, 1)) should throwAn[IllegalArgumentException](message = "cannot remove seam, picture too small")
  }

  "remove horizontal seam with 8x1 picture" >> {
    val shrunk = newSeamCarver("8x1.png")
    shrunk.removeHorizontalSeam(Array[Int](0, 2, 1)) should throwAn[IllegalArgumentException](message = "cannot remove seam, picture too small")
  }

  "remove vertical seam" >> {
    val original = newSeamCarver("3x4.png")
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](1, 1, 1, 1))
    shrunk.width() should_== 2
    shrunk.height() should_== 4
    shrunk.picture.get(1, 1) should_=== original.picture.get(2, 1)
  }

  "remove vertical seam with out of bounds values in the seam" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](4, 3, 2, 1)) should throwAn[IllegalArgumentException](message = "out of bounds at 0")
    shrunk.removeVerticalSeam(Array[Int](2, 3, 2, 1)) should throwAn[IllegalArgumentException](message = "out of bounds at 1")
    shrunk.removeVerticalSeam(Array[Int](0, -1, 0, 1)) should throwAn[IllegalArgumentException](message = "out of bounds at 1")
  }

  "remove vertical seam with jagged seam" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](0, 2, 1, 1)) should throwAn[IllegalArgumentException](message = "too jagged seam at 1")
  }

  "remove horizontal seam with jagged seam" >> {
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeHorizontalSeam(Array[Int](0, 2, 1)) should throwAn[IllegalArgumentException](message = "too jagged seam at 1")
  }

  private def newSeamCarver(filename: String) = new SeamCarver(new Picture(new File(classOf[SeamCarverSpec].getResource(s"/seamCarving/$filename").getFile)))

}
