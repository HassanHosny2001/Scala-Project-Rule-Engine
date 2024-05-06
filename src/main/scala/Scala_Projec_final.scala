import java.io.{File, FileOutputStream, PrintWriter}
import java.security.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.math.BigDecimal.RoundingMode.HALF_UP
import scala.io.{BufferedSource, Source}

import java.text.SimpleDateFormat
import java.util.Date

object Scala_Projec_final extends App {
  //  val source: BufferedSource = Source.fromFile("C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project\\src\\main\\scala\\TRX1000.csv")
  //  val lines: List[String] = source.getLines().drop(1).toList // drop header

  val lines = Source.fromFile("C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project-rule-engine\\src\\main\\scala\\TRX1000.csv").getLines().toList.tail



  def parseDateFromExpiryDate(dateString: String): Date = {
    val dateFormatter = new SimpleDateFormat("mm/dd/yyyy")
    dateFormatter.parse(dateString)
  }

  def parseDateFromTimeStamp(timeStamp: String): Date = {
    val date = timeStamp.substring(0,10)
    val dateFormatter = new SimpleDateFormat("yyyy-mm-dd")
    dateFormatter.parse(date)
  }

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

    // qualifying rules functions
  def lessThan30DaysRemaining(expiryDate: String, transactionDate: String): Boolean = {
    val expiry = parseDateFromExpiryDate(expiryDate)
    val transaction = parseDateFromTimeStamp(transactionDate)
    val daysReamaining = (expiry.getTime - transaction.getTime) / (1000*60*60*24)
    daysReamaining < 30
  }

  def isCheeseOrWine(productName: String): Boolean = {
    productName.toLowerCase.contains("cheese") || productName.toLowerCase.contains("wine")
  }

  def isMarch23rd(timeStamp: String): Boolean = {
    val transactionDate = parseDateFromTimeStamp(timeStamp)
    transactionDate.getMonth == 3 && transactionDate.getDay == 23
  }

  def boughtMoreThan5(quantity: Int): Boolean = {
    quantity > 5
  }

  // calculation rule functions
  def calculateDaysRemainingDiscount(daysRemaining: Long): Double = {
    if (daysRemaining == 29) 0.01
    else if (daysRemaining > 29) 0.0
    else if (daysRemaining >= 1) (30 - daysRemaining) / 100
    else 0.0 /// if expired, then no discount
  }

  def calculateProductDiscount(productName: String): Double = {
    if (productName.toLowerCase.contains("cheese")) 0.1
    else if (productName.toLowerCase.contains("wine")) 0.05
    else 0.0 // no discount for other products
  }

  def calculateMarch23Discount(timeStamp: String): Double = {
    val transactionDate = parseDateFromTimeStamp(timeStamp)
    if (transactionDate.getMonth == 3 && transactionDate.getDay == 23) 0.5
    else 0 // no discount if not on March 23
  }

  def calculateQuantityDiscount(quantity: Int): Double = {
    if (quantity >= 6 && quantity <= 9) 0.05
    else if (quantity >= 10 && quantity <= 14) 0.07
    else if (quantity >= 15) 0.1
    else 0 // no discount for quantity less than 6
  }

  //  --------------------------------------------------- New Requirements ---------------------------------------------------
  def isAppChannel(channel: String): Boolean = {
    channel.toLowerCase == "app"
  }

  def isVisa(paymentMethod: String): Boolean = {
    paymentMethod.toLowerCase == "visa"
  }

  def calculateQuantityDiscountRounded(quantity: Int): Double = {
    val roundedQuantity = Math.ceil(quantity.toDouble / 5) * 5

    if (roundedQuantity <= 5) 0.05
    else if (roundedQuantity <= 10) 0.1
    else if (roundedQuantity <= 15) 0.15
    else 0.2 // this is an assumption for quantities larger than 15
  }

  def calculateVisaDiscount(paymentMethod: String): Double = {
    if (paymentMethod.toLowerCase == "visa") 0.05
    else 0.0
  }

  //  -------------------------------------------------------------------------------------------------------------------------

  val mappedInput = lines.map(lines => lines.split(',')).map(fields => (fields(0), fields(1), fields(2), fields(3).toInt, fields(4).toDouble, fields(5), fields(6)))

  //  val rule1 = mappedInput.map(x => lessThan30DaysRemaining(x._3, x._1))

  val discounts = mappedInput.map { case(timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod) =>

    val transactionDate = parseDateFromTimeStamp(timestamp)
    val expiry = parseDateFromExpiryDate(expiryDate)

    val daysDiscount = if (lessThan30DaysRemaining(expiryDate, timestamp)) calculateDaysRemainingDiscount((expiry.getTime - transactionDate.getTime) / (1000 * 60 * 60 * 24)) else 0.0
    val productDiscount = if (isCheeseOrWine(productName)) calculateProductDiscount(productName) else 0.0
    val March23Discount = if (isMarch23rd(timestamp)) calculateMarch23Discount(timestamp) else 0.0
    val quantityDiscount = if (boughtMoreThan5(quantity)) calculateQuantityDiscount(quantity) else 0.0
    //  --------------------------------------------------- New Requirements ---------------------------------------------------
    val appChannel = if (isAppChannel(channel)) calculateQuantityDiscountRounded(quantity) else 0.0
    val visa = if (isVisa(paymentMethod)) calculateVisaDiscount(paymentMethod) else 0.0

    val allDiscounts = List(daysDiscount, productDiscount, March23Discount, quantityDiscount, appChannel, visa).filter(x => x>0).sorted.reverse.take(2)
//    println(allDiscounts)
    val finalDiscount = if (allDiscounts.nonEmpty) (allDiscounts.sum / allDiscounts.length) else 0.0
    val roundedDiscount = BigDecimal(finalDiscount).setScale(2, HALF_UP)
//        println(avgDiscount)
    val totalPrice = (unitPrice * quantity) - (unitPrice * quantity * finalDiscount)
    val roundedPrice = BigDecimal(totalPrice).setScale(2, HALF_UP)
//        println(roundedPrice)

    (timestamp, productName, expiryDate, quantity, unitPrice, channel, paymentMethod, roundedDiscount, roundedPrice)
  }

//  val f: File = new File("C:\\Users\\Hassan Hosny\\IdeaProjects\\scala-project-rule-engine\\src\\main\\scala\\processed_orders_after_new_rules.csv")
//  val writer = new PrintWriter(new FileOutputStream(f,true))
//  writer.println("Timestamp,Product Name,Expiry Date,Quantity,Unit Price,Channel,Payment Method,Discount,Final Price")
//
  discounts.foreach{case (timestamp, productName, expiryDate, quantity, untiPrice, channel, paymentMethod, finalDiscount, finalPrice) =>
    println(s"$timestamp, $productName, $expiryDate, $quantity, $untiPrice, $channel, $paymentMethod, $finalDiscount, $finalPrice")
  }
//
//  writer.close()

}
