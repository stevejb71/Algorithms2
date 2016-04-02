import java.awt.Color

import edu.princeton.cs.algs4.Picture
import org.specs2.mutable.Specification

class PictureFactorySpec extends Specification {
  val picture = {
    val p = new Picture(5, 3)
    val pixels = List(
      List(0, 1, 2, 3, 4),
      List(10, 11, 12, 13, 14),
      List(20, 21, 22, 23, 24)
    )
    pixels.zipWithIndex.foreach { case (row, y) =>
      row.zipWithIndex.foreach { case (value, x) =>
        p.set(x, y, new Color(value, value * 3, value * 5))
      }
    }
    p
  }

  "can create a PictureFactory from a Picture" >> {
    val pictureFactory = PictureFactory.fromPicture(picture)
    pictureFactory.create() should_=== picture
  }

  "can remove a vertical seam" >> {
    val pictureFactory = PictureFactory.fromPicture(picture)
    val removed = pictureFactory.removeVerticalSeam(Array(1, 0, 4))
    toList(removed) should_=== List(
      List(0, 2, 3, 4),
      List(11, 12, 13, 14),
      List(20, 21, 22, 23)
    )
  }

  "can remove 2 vertical seams" >> {
    val pictureFactory = PictureFactory.fromPicture(picture)
    val removed = pictureFactory.removeVerticalSeam(Array(1, 0, 4)).removeVerticalSeam(Array(0, 0, 2))
    toList(removed) should_=== List(
      List(2, 3, 4),
      List(12, 13, 14),
      List(20, 21, 23)
    )
  }

  "can remove a horizontal seam" >> {
    val pictureFactory = PictureFactory.fromPicture(picture)
    val removed = pictureFactory.removeHorizontalSeam(Array(1, 2, 0, 0, 2))
    toList(removed) should_=== List(
      List(0, 1, 12, 13, 4),
      List(20, 11, 22, 23, 14)
    )
  }


  def toList(p: PictureFactory): List[List[Int]] = (0 until p.height).map(y =>
    (0 until p.width).map(x => {
      val c = p.get(x, y)
      c.getRed
    }).toList
  ).toList
}
