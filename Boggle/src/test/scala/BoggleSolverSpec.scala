import org.specs2.mutable.Specification

class BoggleSolverSpec extends Specification {
  private val dictionary = Array("A", "AB", "ABC", "ABCD", "ABCDE", "ABCDEF", "ABCDEFG", "QUBCDEFG", "ABCDEFGH", "ABCDEFGHIIIII", "QUQUQUQUQU")

  "scoreOf" should {
    "be 0 for a word not in the dictionary" >> {
      scoreOf("XYZ") should_=== 0
    }

    "be 0 for a 1 letter word in the dictionary" >> {
      scoreOf("A") should_=== 0
    }

    "be 1 for a 3 letter word in the dictionary" >> {
      scoreOf("ABC") should_=== 1
    }

    "be 1 for a 4 letter word in the dictionary" >> {
      scoreOf("ABCD") should_=== 1
    }

    "be 2 for a 5 letter word in the dictionary" >> {
      scoreOf("ABCDE") should_=== 2
    }

    "be 3 for a 6 letter word in the dictionary" >> {
      scoreOf("ABCDEF") should_=== 3
    }

    "be 5 for a 7 letter word in the dictionary" >> {
      scoreOf("ABCDEFG") should_=== 5
    }

    "be 11 for an 8 letter word in the dictionary" >> {
      scoreOf("ABCDEFGH") should_=== 11
    }

    "be 11 for an word more than 8 letters long in the dictionary" >> {
      scoreOf("ABCDEFGHIIIII") should_=== 11
    }

    "be 11 for an 8 letter word in the dictionary if QU is a letter pair" >> {
      scoreOf("QUBCDEFG") should_=== 11
    }
  }
  private def scoreOf(word: String) = new BoggleSolver(dictionary).scoreOf(word)
}
