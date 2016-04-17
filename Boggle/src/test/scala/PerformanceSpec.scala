import org.specs2.mutable.Specification

class PerformanceSpec extends Specification {
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
}
