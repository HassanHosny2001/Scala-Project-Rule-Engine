# Scala Project Documentation

# Scala Project Documentation: Rule-Based Discount Calculation and Database Integration

## 1. Introduction:

This Scala project is designed to process transactional data from a CSV file, apply various discount rules based on transactional attributes, and store the processed data into a PostgreSQL database. It leverages Scala for programming and JDBC for database connectivity.

## 2. Project Structure:

### 2.1. Main Scala Object ***(Scala_Projec_final)***:

- **Description:**
    - Serves as the main entry point of the application.
    - Orchestrates data processing, discount calculation, file handling, and database integration.

- **Initialization:**
    - Initializes a logger to record application events.
    - Logs the start of the application.

- **Date Parsing Functions:**
    - Provides functions to parse dates from different formats used in the transactional data.

- **Qualifying Rules Functions:**
    - Implements functions to evaluate various transaction attributes and determine if they meet certain criteria.

- **Calculation Rules Functions:**
    - Calculates discounts based on different rules such as remaining days, product type, transaction date, quantity, payment method, and channel.

- **File Processing:**
    - Reads transactional data from a CSV file and maps it to the required format.
    - Applies discount calculation rules to each transaction.
    - Logs transaction processing details.

- **Database Integration:**
    - Establishes a connection to the PostgreSQL database using JDBC.
    - Inserts processed transaction data into the database.
    - Logs database connectivity events and data insertion status.

- **File Handling:**
    - Manages input and output file paths for transactional data.
    - Reads data from the input CSV file and writes processed data to a new CSV file.

- **Logging:**
    - Utilizes the Scala Logging library to log informational and error messages.
    - Logs application events, transaction details, and errors during date parsing, file handling, and database operations.

### 2.2. DatabaseConnector Class:

- **Description:**
    - Handles database connectivity setup and teardown.
    - Provides methods to establish a connection to the PostgreSQL database.

- **Database Connection Setup:**
    - Defines the database URL, username, and password for establishing a connection.

- **Connection Method:**
    - Implements a method to connect to the database using the provided credentials.
    - Returns a connection object upon successful connection.

## 3. Implementation Details:

### 3.1. Discount Calculation Functions:

- **Description:**
    - Parses dates from strings representing expiry dates and timestamps.
    - Implements rules functions to calculate discounts based on transaction attributes.

### 3.2. File Processing:

- **Description:**
    - Reads transactional data from a CSV file.
    - Maps input data to the required format.
    - Applies discount calculation rules to each transaction.
    - Logs transaction processing details.

### 3.3. Database Integration:

- **Description:**
    - Establishes a connection to the PostgreSQL database using JDBC.
    - Inserts processed transaction data into the database.
    - Logs database connectivity events and data insertion status.

## 4. File Handling:

- **Description:**
    - Defines input and output file paths for transactional data.
    - Reads transactional data from the input CSV file.
    - Writes processed data to a new CSV file.

## 5. Logging:

- **Description:**
    - Utilizes the Scala Logging library for logging.
    - Logs informational messages for application events such as start and completion.
    - Logs transaction processing details.
    - Logs error messages for exceptions during date parsing, file handling, and database operations.
