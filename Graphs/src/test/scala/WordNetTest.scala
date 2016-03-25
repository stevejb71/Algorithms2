import org.junit.Test
import org.junit.Assert._
import collection.JavaConverters._

class WordNetTest {
  @Test(expected = classOf[IllegalArgumentException])
  def throws_if_given_a_cycle() {
    new WordNet(Files.wordNetFile("synsets6.txt"), Files.wordNetFile("hypernyms6InvalidCycle.txt"))
  }

  @Test(expected = classOf[IllegalArgumentException])
  def throws_if_has_two_roots() {
    new WordNet(Files.wordNetFile("synsets6.txt"), Files.wordNetFile("hypernyms6InvalidTwoRoots.txt"))
  }

  @Test
  def isNoun_is_true_if_it_is_passed_a_noun_from_some_synset() {
    val wordNet = new WordNet(Files.wordNetFile("synsets100-subgraph.txt"), Files.wordNetFile("hypernyms100-subgraph.txt"))
    assertTrue(wordNet.isNoun("gamma_globulin"))
  }

  @Test
  def isNoun_is_false_if_it_is_passed_a_noun_not_in_any_synset() {
    val wordNet = new WordNet(Files.wordNetFile("synsets100-subgraph.txt"), Files.wordNetFile("hypernyms100-subgraph.txt"))
    assertFalse(wordNet.isNoun("roger_rabbit"))
  }

  @Test def nouns_returns_a_known_noun() {
    val wordNet = new WordNet(Files.wordNetFile("synsets100-subgraph.txt"), Files.wordNetFile("hypernyms100-subgraph.txt"))
    assertTrue(wordNet.nouns.asScala.exists(_ == "gamma_globulin"))
  }

  @Test
  def sap_for_worm_and_bird() {
    val wordNet = new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt"))
    val ancestor = wordNet.sap("worm", "bird")
    assertEquals("animal animate_being beast brute creature fauna", ancestor)
  }

  @Test
  def distance_for_white_marlin_and_mileage() {
    val wordNet = new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt"))
    val distance = wordNet.distance("white_marlin", "mileage")
    assertEquals(23, distance)
  }

  @Test
  def distance_for_American_water_spaniel_and_histology() {
    val wordNet = new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt"))
    val distance = wordNet.distance("American_water_spaniel", "histology")
    assertEquals(27, distance)
  }

  @Test
  def distance_for_American_water_spaniel_and_histology_is_fast() {
    val wordNet = new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt"))
    for(i <- 0 to 20) {
      wordNet.distance("American_water_spaniel", "histology")
    }
    val startNanos = System.nanoTime()
    val distance = wordNet.distance("American_water_spaniel", "histology")
    val endNanos = System.nanoTime()
    assertTrue((endNanos - startNanos).toString, endNanos - startNanos < 0.1e6)
    assertEquals(27, distance)
  }

  @Test
  def distance_for_multiple_American_water_spaniel_and_histology_is_fast() {
    val wordNet = new WordNet(Files.wordNetFile("synsets.txt"), Files.wordNetFile("hypernyms.txt"))
    for(i <- 0 to 20) {
      wordNet.distance("American_water_spaniel", "histology")
    }
    val startNanos = System.nanoTime()
    for(i <- 0 to 20) {
      wordNet.distance("American_water_spaniel", "histology")
    }
    val distance = wordNet.distance("American_water_spaniel", "histology")
    val endNanos = System.nanoTime()
    assertTrue((endNanos - startNanos).toString, endNanos - startNanos < 1.0e6)
    assertEquals(27, distance)
  }
}