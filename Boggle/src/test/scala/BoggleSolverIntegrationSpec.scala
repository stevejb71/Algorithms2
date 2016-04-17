import edu.princeton.cs.algs4.In
import org.specs2.mutable.Specification
import collection.JavaConverters._

class BoggleSolverIntegrationSpec extends Specification {
  "4x4 board with algs dictionary" >> {
    val (score, words) = test("dictionary-algs4.txt", "board4x4.txt")
    words should not contain "US"
    score should_=== 33
    words.size should_=== 29
  }

  "board-q with algs dictionary" >> {
    val (_, words) = test("dictionary-algs4.txt", "board-q.txt")
    words should contain("REQUIRE")
    words.size should_=== 29
  }

  "board4x4 with yawl dictionary" >> {
    val (_, words) = test("dictionary-yawl.txt", "board4x4.txt")
    words.size should_=== 204
  }

  "board-points4410 with yawl dictionary" >> {
    val (_, words) = test("dictionary-yawl.txt", "board-points4410.txt")
    words.size should_=== 1360
  }

  "word ending in QU" >> {
    def a(x: String) = x.toCharArray.filter(_ != ' ')
    val board = new BoggleBoard(Array(
      a("I  C  R  H  W  H  E  C  D  F"),
      a("O  A  B  S  T  Q  O  W  B  H"),
      a("N  O  H  V  E  I  W  N  P  I"),
      a("E  B  G  H  A  E  A  H  P  A"),
      a("O  L  U  N  W  T  T  N  O  D")
    ))
    val solver = Loader.loadBoggleSolver("dictionary-common.txt")

    val words = solver.getAllValidWords(board).asScala
    words should not contain "SEQ"
    words should not contain "SEQU"
    words.size should_=== 172
  }

//  "lots of qs" >> {
//    val (_, words) = test("dictionary-16q.txt", "board-16q.txt")
//    words.size should_=== 15
//  }

  "load test" >> {
    Loader.loadBoggleSolver("dictionary-zingarelli2005.txt")
    true
  }

  private def test(dictionaryFilename: String, boardFilename: String): (Int, List[String]) = {
    val dictionaryUrl = classOf[BoggleSolverIntegrationSpec].getResource(dictionaryFilename)
    val boardUrl = classOf[BoggleSolverIntegrationSpec].getResource(boardFilename)
    val in = new In(dictionaryUrl)
    val dictionary = in.readAllStrings()
    val solver = new BoggleSolver(dictionary)
    val board = new BoggleBoard(boardUrl.getFile)
    val words = solver.getAllValidWords(board).asScala.toList
    val score = words.foldLeft(0)((sc, w) => sc + solver.scoreOf(w))
    (score, words)
  }
}
