import edu.princeton.cs.algs4.Digraph
import edu.princeton.cs.algs4.In
import org.junit.Test
import org.junit.Assert.assertEquals

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

  @Test
  def length(): Unit = {
    assertEquals(4, sap.length(3, 11))
    assertEquals(3, sap.length(9, 12))
    assertEquals(4, sap.length(7, 2))
    assertEquals(-1, sap.length(1, 6))
  }

  @Test
  def ancestor(): Unit = {
    assertEquals(1, sap.ancestor(3, 11))
    assertEquals(5, sap.ancestor(9, 12))
    assertEquals(0, sap.ancestor(7, 2))
    assertEquals(-1, sap.ancestor(1, 6))
  }
}