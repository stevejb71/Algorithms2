import org.junit.Test
import org.junit.Assert._

class OutcastTest {
  private val outcast: Outcast = new Outcast(new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt")))

  @Test def outcast5() {
    assertEquals("table", outcast.outcast(Array("horse", "zebra", "cat", "bear", "table")))
  }

  @Test def outcast8() {
    assertEquals("bed", outcast.outcast(Array("water", "soda", "bed", "orange_juice", "milk", "apple_juice", "tea", "coffee")))
  }

  @Test def outcast11() {
    assertEquals("potato", outcast.outcast(Array("apple", "pear", "peach", "banana", "lime", "lemon", "blueberry", "strawberry", "mango", "watermelon", "potato")))
  }
}