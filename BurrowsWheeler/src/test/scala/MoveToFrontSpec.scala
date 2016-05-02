import org.specs2.mutable.Specification

class MoveToFrontSpec extends Specification {
  "encode works" >> {
    val message = "ABRACADABRA!"
    val reader = new CharArrayReader(message.toCharArray)
    val writer = new ListBufferWriter

    MoveToFrontImpl.encode(reader, writer)

    writer.out.toList should_=== List(65, 66, 82, 2, 68, 1, 69, 1, 4, 4, 2, 38)
  }

  "decode works" >> {
    val encoded = List(65, 66, 82, 2, 68, 1, 69, 1, 4, 4, 2, 38).map(_.asInstanceOf[Char]).toArray
    val reader = new CharArrayReader(encoded)
    val writer = new ListBufferWriter

    MoveToFrontImpl.decode(reader, writer)

    writer.out.map(_.asInstanceOf[Char]).toList should_=== "ABRACADABRA!".toList
  }
}
