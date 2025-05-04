# **Text Processing Tool for DataFlow Solutions**  

## **Overview**  
This project is a high-performance, automated text-processing system designed to address the inefficiencies faced by **DataFlow Solutions**, a data analytics and reporting company. The tool leverages **regular expressions (regex)**, **Java Streams API**, and **file handling** to streamline text search, extraction, and manipulation. Additionally, it provides a **JavaFX-based user interface** for interactive text processing and data management.  

## **Key Features**  

### **1. Advanced Text Search & Extraction**  
- **Regex Support**: Search, match, and replace text using complex regex patterns.  
  - Supports sets (`[a-z]`), ranges (`\d`, `\w`), alternations (`|`), and quantifiers (`*`, `+`, `?`).  
- **Dynamic Text Operations**:  
  - Find all matches of a given pattern.  
  - Replace matched text with specified substitutions.  

### **2. Automated File Processing**  
- **Efficient File Handling**:  
  - Read large files using `BufferedReader` and `FileReader`.  
  - Write processed data using `BufferedWriter` and `FileWriter`.  
- **Batch Processing**:  
  - Automate repetitive tasks like data cleanup, formatting, and extraction.  

### **3. Optimised Data Processing with Java Streams**  
- **Stream-Based Operations**:  
  - Filter, transform, and aggregate text data efficiently.  
  - Implement word frequency analysis and pattern recognition.  
- **Map-Reduce Techniques**:  
  - Summarise large text datasets using parallel processing.  

### **4. Data Management Module**  
- **Java Collections Integration**:  
  - Use `Set`, `Hashmap` and `Map` for structured data storage.  
  - Support CRUD (Create, Read, Update, Delete) operations on collections.  
- **Custom Object Handling**:  
  - Properly implement `hashCode()` and `equals()` for data integrity.  

### **5. Robust Error Handling & Logging**  
- **Graceful Error Recovery**:  
  - Handle invalid inputs, file errors, and system failures.  
- **Activity Logging**:  
  - Track processing performance, errors, and debugging information.  

### **6. User Interface (JavaFX)**  
- **Interactive Text Processing**:  
  - Input text data and regex patterns via GUI.  
  - View match results and replace text.  
- **Collection Management**:  
  - Add, update, and delete entries in data collections.  
