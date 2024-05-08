# Scala Project Documentation

## Overview
This Scala project is designed to process transactions stored in a CSV file, apply various qualifying and calculation rules to determine discounts, and then store the processed data in a database. It utilizes Scala's functional programming features and libraries for file handling, date manipulation, logging, and database connectivity.

## Components
### 1. Main Object: Scala_Projec_final
- Entry point of the application.
- Reads data from a CSV file, processes transactions, calculates discounts, and stores results in a database.
- Contains functions for parsing dates, applying qualifying rules, and calculating discounts.

### 2. Libraries and Packages
- **Logging Library**: `com.typesafe.scalalogging.Logger` for logging application events.
- **File Manipulation**: `java.io.File`, `java.io.FileOutputStream`, `java.io.PrintWriter` for reading from and writing to files.
- **Rounding Mode**: `scala.math.BigDecimal.RoundingMode.HALF_UP` for rounding numbers.
- **Reading from File**: `scala.io.BufferedSource`, `scala.io.Source` for reading data from a file.
- **Date Formatting**: `java.text.SimpleDateFormat`, `java.util.Date` for parsing and handling dates.
- **Database Connectivity**: `java.sql.Connection`, `java.sql.DriverManager` for connecting to and interacting with a database.

### 3. Qualifying Rules Functions
- `lessThan30DaysRemaining`: Checks if there are less than 30 days remaining between two dates.
- `isCheeseOrWine`: Checks if a product name contains "cheese" or "wine".
- `isMarch23rd`: Checks if a transaction occurred on March 23rd.
- `boughtMoreThan5`: Checks if quantity bought is more than 5.
- `isAppChannel`: Checks if channel is "app".
- `isVisa`: Checks if payment method is "visa".

### 4. Calculation Rules Functions
- `calculateDaysRemainingDiscount`: Calculates discount based on remaining days.
- `calculateProductDiscount`: Calculates discount based on product name.
- `calculateMarch23Discount`: Calculates discount based on transaction date (March 23rd).
- `calculateQuantityDiscount`: Calculates discount based on quantity bought.
- `calculateQuantityDiscountRounded`: Calculates rounded quantity discount.
- `calculateVisaDiscount`: Calculates visa discount.

### 5. Reading from File and Processing
- Reads data from a CSV file, maps input data to required format, and calculates discounts for each transaction.

### 6. Database Connection and Insertion
- Establishes a connection to the database using JDBC.
- Inserts processed data into the database.

## Usage
1. Ensure Scala and necessary dependencies are installed.
2. Update file paths and database connection details as per your environment.
3. Run the Scala project to process transactions and store results in the database.

## Conclusion
This Scala project demonstrates how functional programming concepts can be used to implement a transaction processing system efficiently. By applying qualifying and calculation rules, it determines discounts for each transaction and maintains data integrity by storing processed results in a database.
