import org.specs2.mutable.Specification

class PerformanceSpec extends Specification {
  // 10296 with TrieST (26)
  // 14017 with boolean array in Path
  // 23451 removing arraydeque in Path.
  // 29629 removing a replaceAll
  "performance should be at worst 2x reference solution" >> {
    val solver = Loader.loadBoggleSolver("dictionary-yawl.txt")
    val board = Loader.loadBoard("board4x4.txt")
    (0 to 20).foreach(_ => solver.getAllValidWords(board))
    val end = 5000 + System.currentTimeMillis()
    var count = 0
    while (System.currentTimeMillis() < end) {
      solver.getAllValidWords(board)
      count += 1
    }
    count should be greaterThan 25000
  }

  "constructor performance should be at worst 5x reference solution" >> {
    (0 to 5).foreach(_ => Loader.loadBoggleSolver("dictionary-zingarelli2005.txt"))
    val start = System.currentTimeMillis()
    (0 to 50).foreach(_ => Loader.loadBoggleSolver("dictionary-zingarelli2005.txt"))
    val end = System.currentTimeMillis()
    val timeMs = (end - start).asInstanceOf[Double] / 50.0
    timeMs should be lessThan 280.0
  }
}
