import java.util.function.Predicate

import org.specs2.mutable.Specification

class DFSSpec extends Specification {
  "quick test on 2x2" >> {
    val board = new BoggleBoard(Array(Array('A', 'B'), Array('C', 'D')))
    val graph = new Graph(board)
    val dfs = new DFS()
    val paths = new scala.collection.mutable.ArrayBuffer[Path]
    dfs.dfs(graph, 0, 0, new Predicate[Path] {
      override def test(path: Path): Boolean = {
        paths += path.copy()
        true
      }
    })
    paths.size should_=== 16
    paths.toSet.size should_=== 16
  }

}
