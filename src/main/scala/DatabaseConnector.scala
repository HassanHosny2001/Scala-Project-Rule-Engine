import java.sql.{Connection, DriverManager}


//  --------------------------------------------------- DataBase Connection ---------------------------------------------------

class DatabaseConnector {

  def connectToDB(url: String, username: String, password: String): Connection = {
    var conn: Connection = null
    try {
      Class.forName("org.postgresql.Driver")
      conn = DriverManager.getConnection(url, username, password)
      if (conn != null) {
        println("Connection Established")
      } else {
        println("Connection Failed")
      }
    } catch {
      case e: Exception => println(e)
    }
    conn
  }
}
