// Import necessary libraries and packages
import com.typesafe.scalalogging.Logger // Logging library
import java.io.{File, FileOutputStream, PrintWriter} // File manipulation
import scala.math.BigDecimal.RoundingMode.HALF_UP // Rounding mode for BigDecimal
import scala.io.{BufferedSource, Source} // Reading from file
import java.text.SimpleDateFormat // Date formatting
import java.util.Date // Date handling
import java.sql.{Connection, DriverManager} // Database connectivity

object Scala_Projec_final extends App {
  //  val source: BufferedSource = Source.fromFile("C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project\\src\\main\\scala\\TRX1000.csv")
  //  val lines: List[String] = source.getLines().drop(1).toList // drop header

  //  // function to parse date String to localDate from expiryDate
  //  def parseDateFromExpiryDate(dateString: String): LocalDate = {
  //    LocalDate.parse(dateString)
  ////    val dateFormatter = DateTimeFormatter.ofPattern("m/dd/yyyy")
  ////    LocalDate.parse(dateString, dateFormatter)
  //  }
  //
  //  // function to parse date String to localDate from timeStamp
  //  def parseDateFromTimeStamp(timeStamp: String): LocalDate = {
  //    val date = timeStamp.substring(0,10)
  //    LocalDate.parse(date)
  //  }

  //  def lessThan30DaysRemaining(expiryDate: Date, transactionDate: Date): Boolean = {
  //    val daysRemaining = (expiryDate.getTime - transactionDate.getTime) / (1000 * 60 * 60 * 24)
  //    daysRemaining < 30
  //  }

  // Initialize a logger
  val logger = Logger(getClass.getName)

  // Log a message indicating the start of the application
  logger.info("Starting the application...")

  // Define a function to parse a date from a string representing an expiry date
  def parseDateFromExpiryDate(dateString: String): Date = {
    // Attempt to parse the date using SimpleDateFormat
    try {
      val dateFormatter = new SimpleDateFormat("MM/dd/yyyy")
      dateFormatter.parse(dateString)
    } catch {
      // Log an error if parsing fails
      case ex: Exception =>
        logger.error(s"An error occurred during date parsing from ExpityDate: $dateString", ex)
        throw ex
    }
  }

  // Define a function to parse a date from a string representing a timestamp
  def parseDateFromTimeStamp(timeStamp: String): Date = {
    // Attempt to parse the date using SimpleDateFormat
    try {
      val date = timeStamp.substring(0, 10)
      val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
      dateFormatter.parse(date)
    } catch {
      // Log an error if parsing fails
      case ex: Exception =>
        logger.error(s"An error occurred during date parsing from TimeStamp: $timeStamp", ex)
        throw ex
    }
  }

  //  ===========================================================================================================================
  //  =============================================== Qualifying Rules Functions ================================================
  //  ===========================================================================================================================
  // Define a function to check if there are less than 30 days remaining between two dates
  def lessThan30DaysRemaining(expiryDate: String, transactionDate: String): Boolean = {
    try {
      val expiry = parseDateFromExpiryDate(expiryDate)
      val transaction = parseDateFromTimeStamp(transactionDate)
      logger.debug(s"Parsing ExpiryDate and Transaction : $expiryDate and $transactionDate")
      val daysReamaining = (expiry.getTime - transaction.getTime) / (1000 * 60 * 60 * 24)
      daysReamaining < 30
    } catch {
      case ex: Exception =>
        logger.error(s"An error occurred during date parsing from TimeStamp and ExpiryDate: $expiryDate and $transactionDate", ex)
        throw ex
    }
  }

  // Define a function to check if a product name contains "cheese" or "wine"
  def isCheeseOrWine(productName: String): Boolean = {
    productName.toLowerCase.contains("cheese") || productName.toLowerCase.contains("wine")
  }

  // Define a function to check if a transaction occurred on March 23rd
  def isMarch23rd(timeStamp: String): Boolean = {
    val transactionDate = parseDateFromTimeStamp(timeStamp)
    transactionDate.getMonth == 3 && transactionDate.getDay == 23
  }

  // Define a function to check if quantity bought is more than 5
  def boughtMoreThan5(quantity: Int): Boolean = {
    quantity > 5
  }
  //  =============================================== NEW Qualifying Rules Functions ================================================
  // Define a function to check if channel is "app"
  def isAppChannel(channel: String): Boolean = {
    channel.toLowerCase == "app"
  }

  // Define a function to check if payment method is "visa"
  def isVisa(paymentMethod: String): Boolean = {
    paymentMethod.toLowerCase == "visa"
  }

  //  ===========================================================================================================================
  //  =============================================== Calculation Rules functions ===============================================
  //  ===========================================================================================================================
  // Define a function to calculate discount based on remaining days
  def calculateDaysRemainingDiscount(daysRemaining: Long): Double = {
    if (daysRemaining == 29) 0.01
    else if (daysRemaining > 29) 0.0
    else if (daysRemaining >= 1) (30 - daysRemaining) / 100
    else 0.0 /// if expired, then no discount
  }

  // Define a function to calculate discount based on product name
  def calculateProductDiscount(productName: String): Double = {
    if (productName.toLowerCase.contains("cheese")) 0.1
    else if (productName.toLowerCase.contains("wine")) 0.05
    else 0.0 // no discount for other products
  }

  // Define a function to calculate discount based on transaction date (March 23rd)
  def calculateMarch23Discount(timeStamp: String): Double = {
    val transactionDate = parseDateFromTimeStamp(timeStamp)
    if (transactionDate.getMonth == 3 && transactionDate.getDay == 23) 0.5
    else 0 // no discount if not on March 23
  }

  // Define a function to calculate discount based on quantity bought
  def calculateQuantityDiscount(quantity: Int): Double = {
    if (quantity >= 6 && quantity <= 9) 0.05
    else if (quantity >= 10 && quantity <= 14) 0.07
    else if (quantity >= 15) 0.1
    else 0 // no discount for quantity less than 6
  }
  //  =============================================== NEW Calculation Rules functions ===============================================
  // Define a function to calculate rounded quantity discount
  def calculateQuantityDiscountRounded(quantity: Int): Double = {
    val roundedQuantity = Math.ceil(quantity.toDouble / 5) * 5

    if (roundedQuantity <= 5) 0.05
    else if (roundedQuantity <= 10) 0.1
    else if (roundedQuantity <= 15) 0.15
    else 0.2 // this is an assumption for quantities larger than 15
  }

  // Define a function to calculate visa discount
  def calculateVisaDiscount(paymentMethod: String): Double = {
    if (paymentMethod.toLowerCase == "visa") 0.05
    else 0.0
  }
  //  ================================================================================================================================
  // Reading from file and processing...
  val raw_data = "C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project-rule-engine\\src\\raw_data\\TRX1000.csv"
  logger.info(s"Reading data from file $raw_data")
  val lines = Source.fromFile(raw_data).getLines().toList.tail

  // Map input data to required format
  val mappedInput = lines.map(lines => lines.split(',')).map(fields => (fields(0), fields(1), fields(2), fields(3).toInt, fields(4).toDouble, fields(5), fields(6)))

  // Calculate discounts for each transaction
  val discounts = mappedInput.map { case(timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod) =>

    val transactionDate = parseDateFromTimeStamp(timestamp)
    val expiry = parseDateFromExpiryDate(expiryDate)

    // Calculate various discounts based on rules
    val daysDiscount = if (lessThan30DaysRemaining(expiryDate, timestamp)) calculateDaysRemainingDiscount((expiry.getTime - transactionDate.getTime) / (1000 * 60 * 60 * 24)) else 0.0
    val productDiscount = if (isCheeseOrWine(productName)) calculateProductDiscount(productName) else 0.0
    val March23Discount = if (isMarch23rd(timestamp)) calculateMarch23Discount(timestamp) else 0.0
    val quantityDiscount = if (boughtMoreThan5(quantity)) calculateQuantityDiscount(quantity) else 0.0
    //  ====================================================== New Requirements ======================================================
    val appChannel = if (isAppChannel(channel)) calculateQuantityDiscountRounded(quantity) else 0.0
    val visa = if (isVisa(paymentMethod)) calculateVisaDiscount(paymentMethod) else 0.0
    //  ==============================================================================================================================
    // Aggregate discounts and calculate final price
    val allDiscounts = List(daysDiscount, productDiscount, March23Discount, quantityDiscount, appChannel, visa).filter(x => x>0).sorted.reverse.take(2)
//    println(allDiscounts)
    val finalDiscount = if (allDiscounts.nonEmpty) (allDiscounts.sum / allDiscounts.length) else 0.0
    val roundedDiscount = BigDecimal(finalDiscount).setScale(2, HALF_UP)
//        println(avgDiscount)
    val totalPrice = (unitPrice * quantity) - (unitPrice * quantity * finalDiscount)
    val roundedPrice = BigDecimal(totalPrice).setScale(2, HALF_UP)
//        println(roundedPrice)

    // Log information about the transaction being processed
    logger.info(s"Transaction that being processed: $productName, $quantity, $unitPrice, $channel, $paymentMethod")

    // Return tuple containing transaction details and calculated discounts
    (timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod, roundedDiscount, roundedPrice)
  }

  // Writing to file...
  val output_data = "C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project-rule-engine\\src\\output_data\\processed_orders_after_new_rules.csv"
  logger.info(s"Writing data to file: $output_data ")
  val f: File = new File(output_data)
  val writer = new PrintWriter(new FileOutputStream(f,true))
  writer.println("Timestamp,Product Name,Expiry Date,Quantity,Unit Price,Channel,Payment Method,Discount,Final Price")

  discounts.foreach{case (timestamp, productName, expiryDate, quantity, untiPrice, channel, paymentMethod, finalDiscount, finalPrice) =>
    writer.println(s"$timestamp, $productName, $expiryDate, $quantity, $untiPrice, $channel, $paymentMethod, $finalDiscount, $finalPrice")
  }
//
  writer.close()
  logger.info(s"Data written to file: $output_data ")

  //  ===========================================================================================================================
  //  =================================================== DataBase Connection ===================================================
  //  ===========================================================================================================================

  // Establishing database connection
  logger.info("Establishing database connection...")
  val url = "jdbc:postgresql://localhost:5432/rule_engine_scala"
  val username = "postgres"
  val password = "admin"

  val connector = new DatabaseConnector()
  val connection = connector.connectToDB(url, username, password)

  discounts.foreach { case (timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod, finalDiscount, finalPrice) =>
    insertData(timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod, finalDiscount.toDouble, finalPrice.toDouble, connection)
  }

  // Inserting data into the database...
  def insertData(timestamp: String, productName: String, expiryDate: String, quantity: Int, unitPrice: Double, channel: String, paymentMethod: String, discount: Double, finalPrice: Double, connection: Connection) = {
    try{
      val query = "INSERT INTO processed_orders (timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod, discount, finalPrice)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
      val statement = connection.prepareStatement(query)
      statement.setString(1, timestamp)
      statement.setString(2, productName)
      statement.setString(3, expiryDate)
      statement.setInt(4, quantity)
      statement.setDouble(5, unitPrice)
      statement.setString(6, channel)
      statement.setString(7, paymentMethod)
      statement.setDouble(8, discount)
      statement.setDouble(9, finalPrice)
      statement.executeUpdate()
    } catch {
      case ex: Exception =>
        logger.error("Failed to insert data into database", ex)
    }
  }

  // Close database connection
  logger.info(s"Closing database connection with $url that using Username: $username")
  connection.close()

  // Log message indicating completion of application execution
  logger.info("Application execution completed.")

}
