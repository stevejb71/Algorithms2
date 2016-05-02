import org.specs2.mutable.Specification

class BurrowsWheelerSpec extends Specification {
  "encode works" >> {
    val writer = new ListBufferWriter

    BurrowsWheelerImpl.encode(new CharArrayReader("ABRACADABRA!".toCharArray), writer)

    writer.out.toList should_=== List(3, 65, 82, 68, 33, 82, 67, 65, 65, 65, 65, 66, 66)
  }

  "decoding encoded gets the original back" >> {
    val writer = new ListBufferWriter
    val tmpWriter = new ListBufferWriter

    BurrowsWheelerImpl.encode(new CharArrayReader("ABRACADABRA!".toCharArray), tmpWriter)
    BurrowsWheelerImpl.decode(new CharArrayReader(tmpWriter.out.map(_.asInstanceOf[Char]).toArray), writer)

    new String(writer.out.toArray.map(_.asInstanceOf[Char])) should_=== "ABRACADABRA!"
  }
}