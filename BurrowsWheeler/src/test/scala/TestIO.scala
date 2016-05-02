import scala.collection.mutable.ListBuffer

class CharArrayReader(message: Array[Char]) extends IO.Reader {
  var pos = 0
  override def isEmpty = pos == message.length
  override def readChar() = {
    val ch = message(pos)
    pos += 1
    ch
  }
  override def readString() = {
    val s = new String(message.drop(pos))
    pos = message.length
    s
  }

  override def readInt() = {
    val n = message(pos).toInt
    pos += 1
    n
  }
}

class ListBufferWriter extends IO.Writer {
  val out = new ListBuffer[Int]
  override def writeInt(n: Int) = out += n
  override def writeChar(ch: Char) = out += ch.asInstanceOf[Int]
  override def writeString(s: String) = s foreach (ch => out += ch.asInstanceOf[Int])
  override def flush() {}
}

