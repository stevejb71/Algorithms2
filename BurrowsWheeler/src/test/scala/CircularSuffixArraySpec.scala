import org.specs2.mutable.Specification

import scala.util.Random

class CircularSuffixArraySpec extends Specification {
  "constructor throws NPE if the input is null" >> {
    new CircularSuffixArray(null) should throwA[NullPointerException]
  }

  "length should be the same as the length of the input string" >> {
    new CircularSuffixArray("abc").length should_=== 3
  }

  "throws IndexOutOfBoundException if index negative" >> {
    new CircularSuffixArray("abc").index(-1) should throwA[IndexOutOfBoundsException]
  }

  "throws IndexOutOfBoundException if equal to length" >> {
    new CircularSuffixArray("abc").index(3) should throwA[IndexOutOfBoundsException]
  }

  "index[i] is the index of the original suffix that appears ith in the sorted array" >> {
    val array = new CircularSuffixArray("ABRACADABRA!")

    (0 to 11 map array.index).toList should_=== List(11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2)
  }

  "should be as fast as the reference on a long string" >> {
    val random = new Random(100)
    val size = 100000
    val chs = new Array[Char](size)
    (0 until size) foreach { i => {
      chs(i) = random.nextPrintableChar()
    }}
    val s = new String(chs)
    new CircularSuffixArray(s)

    var count = 0
    val start = System.currentTimeMillis()
    0 until 100 foreach {i =>
      count += new CircularSuffixArray(s).index(i % 100)
    }
    val end = System.currentTimeMillis()

    (end - start) should be_<(1000L)
  }
}
