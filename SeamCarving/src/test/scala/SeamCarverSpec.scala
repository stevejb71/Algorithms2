import java.io.File

import edu.princeton.cs.algs4._
import org.specs2.mutable._

class SeamCarverSpec extends Specification {
  "null picture in constructor causes an exception" >> {
    new SeamCarver(null) should throwA[NullPointerException]
  }

  "takes a copy on construction" >> {
    val picture = new Picture(20, 30)
    val seamCarver = new SeamCarver(picture)

    seamCarver.picture() should not beTheSameAs(picture)
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

  "remove vertical seam twice" >> {
    val original = newSeamCarver("3x4.png")
    val shrunk = newSeamCarver("3x4.png")
    shrunk.removeVerticalSeam(Array[Int](1, 1, 1, 1))
    shrunk.removeVerticalSeam(Array[Int](0, 0, 0, 0))
    shrunk.width() should_== 1
    shrunk.height() should_== 4
    shrunk.picture.get(0, 1) should_=== original.picture.get(2, 1)
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
//
//
//  "m-ex1" >> {
//    val s =
//      """    A-G    16
//        |    A-B    15
//        |    A-F    13
//        |    B-C    12
//        |    B-H    11
//        |    B-G     7
//        |    C-D     6
//        |    H-C     3
//        |    H-D    14
//        |    D-E     9
//        |    D-I     4
//        |    D-J     1
//        |    E-J     8
//        |    F-G    17
//        |    H-G    10
//        |    H-I     2
//        |    I-J     5
//      """.stripMargin
//    val lines = s.filter(_ != ' ').filter(_ != '-').lines.toList
//    val g = new EdgeWeightedGraph(10)
//    def addEdge(l: String): Unit = {
//      val v = l.charAt(0).asInstanceOf[Int] - 65
//      val w = l.charAt(1).asInstanceOf[Int] - 65
//      val weight = l.drop(2).toInt
//      g.addEdge(new Edge(v, w, weight))
//    }
//    lines.foreach(addEdge)
//
//    val k = new KruskalMST(g)
//    System.out.println(k.edges().asScala.toList.map(_.weight().asInstanceOf[Int]).mkString(" "))
//    true
//  }
//
//  "ex1" >> {
//    val (a, b, c, d, e, f, g, h) = (0, 1, 2, 3, 4, 5, 6, 7)
//    val digraph = new EdgeWeightedDigraph(8)
//    def addEdge(v: Int, w: Int, weight: Double): Unit = digraph.addEdge(new DirectedEdge(v, w, weight))
//    addEdge(a, e, 15.0)
//    addEdge(b, a, 25.0)
//    addEdge(b, f, 24.0)
//    addEdge(c, b, 7.0)
//    addEdge(c, f, 33.0)
//    addEdge(c, g, 28.0)
//    addEdge(d, c, 33.0)
//    addEdge(d, h, 18.0)
//    addEdge(f, a, 4.0)
//    addEdge(f, e, 24.0)
//    addEdge(f, g, 0.0)
//    addEdge(h, c, 5.0)
//    addEdge(h, g, 35.0)
//    val dijkstra = new DijkstraSP(digraph, d)
//    System.out.println((0 until 8).map(dijkstra.distTo).map(_.asInstanceOf[Int]).mkString(" "))
//    true
//  }
//
//  "ex2" >> {
//    val (a, b, c, d, e, f, g, h) = (0, 1, 2, 3, 4, 5, 6, 7)
//    val digraph = new EdgeWeightedDigraph(8)
//    def addEdge(v: Int, w: Int, weight: Double): Unit = digraph.addEdge(new DirectedEdge(v, w, weight))
//    addEdge(b, a, 39)
//    addEdge(c, b, 46)
//    addEdge(c, d, 9)
//    addEdge(c, f, 27)
//    addEdge(c, g, 15)
//    addEdge(e, a, 30)
//    addEdge(e, b, 4)
//    addEdge(f, b, 15)
//    addEdge(f, e, 31)
//    addEdge(f, g, 2)
//    addEdge(h, c, 0)
//    addEdge(h, d, 13)
//    addEdge(h, g, 18)
//    val sp = new AcyclicSP(digraph, h)
//    System.out.println((0 until 8).map(sp.distTo).map(_.asInstanceOf[Int]).mkString(" "))
//    true
//  }
//
//  "ex3" >> {
//    val (a, b, c, d, e, f, g, h) = (0, 1, 2, 3, 4, 5, 6, 7)
//    val digraph = new EdgeWeightedDigraph(8)
//    def addEdge(v: Int, w: Int, weight: Double): Unit = digraph.addEdge(new DirectedEdge(v, w, weight))
//    addEdge(a, e, 34)
//    addEdge(b, f, 25)
//    addEdge(b, a, 57)
//    addEdge(b, c, 16)
//    addEdge(d, c, 22)
//    addEdge(f, g, 4)
//    addEdge(f, a, 22)
//    addEdge(f, e, 59)
//    addEdge(g, c, 34)
//    addEdge(g, b, 8)
//    addEdge(h, c, 50)
//    addEdge(h, g, 18)
//    addEdge(h, d, 28)
//    val sp = new BellmanFordSP(digraph, h)
//    System.out.println((0 until 8).map(sp.distTo).map(_.asInstanceOf[Int]).mkString(" "))
//    true
//  }

}
