import org.specs2.mutable.Specification

import collection.JavaConverters._

class GraphSpec extends Specification {
  "corner" >> {
    val board = Loader.loadBoard("board4x4.txt")
    val graph = new Graph(board)
    graph.neighbours(graph.get(0, 0)).asScala.toList should containTheSameElementsAs(List(
      new Cube(0, 1, 'A'), new Cube(1, 0, 'T'), new Cube(1, 1, 'P')
    ))
  }
}
