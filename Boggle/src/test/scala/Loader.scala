import edu.princeton.cs.algs4.In

object Loader {
  def loadBoggleSolver(dictionaryFilename: String): BoggleSolver = {
    val dictionaryUrl = classOf[BoggleSolverIntegrationSpec].getResource(dictionaryFilename)
    val in = new In(dictionaryUrl)
    val dictionary = in.readAllStrings()
    new BoggleSolver(dictionary)
  }

  def loadBoard(boardFilename: String): BoggleBoard = {
    val boardUrl = classOf[BoggleSolverIntegrationSpec].getResource(boardFilename)
    new BoggleBoard(boardUrl.getFile)
  }
}
