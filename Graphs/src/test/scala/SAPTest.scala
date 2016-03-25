import edu.princeton.cs.algs4.Digraph
import edu.princeton.cs.algs4.In
import org.junit.Test
import org.junit.Assert._

import util.Random

import collection.JavaConverters._

object SAPTest {
  def readDigraph(filename: String): Digraph = {
    val in = new In(filename)
    val lines = in.readAllLines()
    val vertices = lines(0).toInt
    val digraph = new Digraph(vertices)
    lines.drop(2).map(_.trim).foreach(l => {
      val ids = l.split("\\s+").map(Integer.parseInt)
      val fromId = ids(0)
      ids.tail.foreach(i => digraph.addEdge(fromId, i))
    })
    digraph
  }
}

class SAPTest {
  private val sap = new SAP(SAPTest.readDigraph(Files.wordNetFile("digraph1.txt")))

  @Test(expected = classOf[NullPointerException])
  def lengthNull(): Unit = {
    assertEquals(0, sap.length(null, null))
  }

  @Test
  def length0(): Unit = {
    assertEquals(0, sap.length(3, 3))
  }

  @Test
  def length1(): Unit = {
    assertEquals(4, sap.length(3, 11))
  }

  @Test
  def length2(): Unit = {
    assertEquals(3, sap.length(9, 12))
  }

  @Test
  def length3(): Unit = {
    assertEquals(4, sap.length(7, 2))
  }

  @Test
  def length4(): Unit = {
    assertEquals(-1, sap.length(1, 6))
  }

  @Test
  def length5(): Unit = {
    val sap = new SAP(SAPTest.readDigraph(Files.wordNetFile("digraph3.txt")))
    assertEquals(5, sap.length(14, 7))
  }

  @Test
  def lengthBig(): Unit = {
    val sap = new SAP(SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt")))
    assertEquals(13, sap.length(45077, 39302))
  }

  @Test
  def lengthOverlapping(): Unit = {
    val sap = new SAP(SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt")))
    val v = List(6, 11, 12, 13, 19).map(_.asInstanceOf[java.lang.Integer]).asJava
    val w = List(3, 4, 9, 17, 19).map(_.asInstanceOf[java.lang.Integer]).asJava
    assertEquals(0, sap.length(v, w))
  }

  @Test
  def lengthBigTiming(): Unit = {
    val digraph = SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt"))
    val sap = new SAP(digraph)
    def randomInt() = Random.nextInt(digraph.V())
    var opsCount = 0
    val startTime = System.currentTimeMillis()
    while(System.currentTimeMillis() - startTime < 1000) {
      sap.length(randomInt(), randomInt())
      opsCount += 1
    }
    assertTrue(opsCount.toString, opsCount > 4.0e5)
  }

  @Test
  def multiLengthBigTiming(): Unit = {
    val digraph = SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt"))
    val sap = new SAP(digraph)
    val as = new java.util.ArrayList[java.lang.Integer](5)
    val bs = new java.util.ArrayList[java.lang.Integer](5)
    for(i <- 0 until 5) {
      as.add(0)
      bs.add(0)
    }
    def randomInts() = {
      for(i <- 0 until 5) {
        as.set(i, Random.nextInt(digraph.V()))
        bs.set(i, Random.nextInt(digraph.V()))
      }
    }
    var opsCount = 0
    val startTime = System.currentTimeMillis()
    while(System.currentTimeMillis() - startTime < 1000) {
      randomInts()
      sap.length(as, bs)
      opsCount += 1
    }
    assertTrue(opsCount.toString, opsCount > 4.0e5)
  }

  @Test
  def multiAncestorBigTiming(): Unit = {
    val digraph = SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt"))
    val sap = new SAP(digraph)
    val as = new java.util.ArrayList[java.lang.Integer](5)
    val bs = new java.util.ArrayList[java.lang.Integer](5)
    for(i <- 0 until 5) {
      as.add(0)
      bs.add(0)
    }
    def randomInts() = {
      for(i <- 0 until 5) {
        as.set(i, Random.nextInt(digraph.V()))
        bs.set(i, Random.nextInt(digraph.V()))
      }
    }
    var opsCount = 0
    val startTime = System.currentTimeMillis()
    while(System.currentTimeMillis() - startTime < 1000) {
      randomInts()
      sap.ancestor(as, bs)
      opsCount += 1
    }
    assertTrue(opsCount.toString, opsCount > 4.0e5)
  }

  @Test
  def ancestorBigTiming(): Unit = {
    val digraph = SAPTest.readDigraph(Files.wordNetFile("digraph-wordnet.txt"))
    val sap = new SAP(digraph)
    def randomInt() = Random.nextInt(digraph.V())
    var opsCount = 0
    val startTime = System.currentTimeMillis()
    while(System.currentTimeMillis() - startTime < 1000) {
      sap.ancestor(randomInt(), randomInt())
      opsCount += 1
    }
    assertTrue(opsCount.toString, opsCount > 4.0e5)
  }

  @Test(expected = classOf[NullPointerException])
  def ancestorNull(): Unit = {
    assertEquals(1, sap.ancestor(null, null))
  }

  @Test
  def ancestor1(): Unit = {
    assertEquals(1, sap.ancestor(3, 11))
  }

  @Test
  def ancestor2(): Unit = {
    assertEquals(5, sap.ancestor(9, 12))
  }

  @Test
  def ancestor3(): Unit = {
    assertEquals(0, sap.ancestor(7, 2))
  }

  @Test
  def ancestor4(): Unit = {
    assertEquals(-1, sap.ancestor(1, 6))
  }
}