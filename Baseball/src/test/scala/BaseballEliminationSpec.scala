import java.io.File
import collection.JavaConverters._
import org.specs2.mutable.Specification

class BaseballEliminationSpec extends Specification {
  "teams4 has 4 teams" >> {
    teams4.numberOfTeams should_=== 4
  }

  "teams4 has correct team names" >> {
    teams4.teams.asScala.toList should_=== teamNames4
  }

  "teams4 has correct wins" >> {
    teamNames4.map(teams4.wins) should_=== List(83, 80, 78, 77)
  }

  "teams4 has correct losses" >> {
    teamNames4.map(teams4.losses) should_=== List(71, 79, 78, 82)
  }

  "teams4 has correct remaining" >> {
    teamNames4.map(teams4.remaining) should_=== List(8, 3, 6, 3)
  }

  "teams4 has correct against" >> {
    val againsts = teamNames4.map(t1 => teamNames4.map(t2 => teams4.against(t1, t2)))
    againsts should_=== List(
      List(0, 1, 6, 1),
      List(1, 0, 0, 2),
      List(6, 0, 0, 0),
      List(1, 2, 0, 0)
    )
  }

  "Eliminations in teams4" >> {
    teamNames4.map(teams4.isEliminated) should_=== List(false, true, false, true)
  }

  "Certificates in teams4" >> {
    teamNames4.map(teams4.certificateOfElimination).map(listOrNull) should_=== List(
      null, List("Atlanta", "New_York"), null, List("Atlanta")
    )
  }

  "Certificates in teams5" >> {
    List("New_York", "Baltimore", "Boston", "Toronto", "Detroit").map(new BaseballElimination(filename("teams5.txt")).certificateOfElimination).map(listOrNull) should_=== List(
      null, null, null, null, List("New_York", "Baltimore", "Boston", "Toronto")
    )
  }

  "team10 number of teams" >> {
    new BaseballElimination(filename("teams10.txt")).numberOfTeams() should_=== 10
  }

  "team60 number of teams" >> {
    new BaseballElimination(filename("teams60.txt")).numberOfTeams() should_=== 60
  }

  "invalid team to wins throws exception" >> {
    teams4.wins("Princeton") should throwA[IllegalArgumentException]
  }

  "invalid team to losses throws exception" >> {
    teams4.losses("Princeton") should throwA[IllegalArgumentException]
  }

  "invalid teams to against throws exception" >> {
    teams4.against("Princeton", "Japan") should throwA[IllegalArgumentException]
  }

  "invalid teams to isEliminated throws exception" >> {
    teams4.isEliminated("Princeton") should throwA[IllegalArgumentException]
  }

  "invalid teams to certificateOfElimination throws exception" >> {
    teams4.certificateOfElimination("Princeton") should throwA[IllegalArgumentException]
  }

  private def filename(base: String): String = {
    val filename = classOf[BaseballEliminationSpec].getResource(s"/$base").getFile
    if(!new File(filename).isFile) {
      throw new Exception(s"Not a file: $filename")
    }
    filename
  }

  private def teams4: BaseballElimination = new BaseballElimination(filename("teams4.txt"))
  private val teamNames4: List[String] = List("Atlanta", "Philadelphia", "New_York", "Montreal")
  private def listOrNull[A](xs: java.lang.Iterable[A]): List[A] = if(xs == null) null else xs.asScala.toList
}
