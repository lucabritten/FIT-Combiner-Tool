# 🚴‍♂️ FIT Combiner

**FIT Combiner** is a Java-based tool for merging multiple Garmin `.fit` files into a single combined file.  
It is especially useful for combining split activity recordings into one continuous session, ready for upload to platforms like **Strava** or **Garmin Connect**.

---

## 🧰 Features

- Merge multiple `.fit` files into a single output  
- Preserves key metadata (timestamps, heart rate, distance, speed, etc.)  
- Automatically sorts all records chronologically  
- Uses the official Garmin FIT SDK  
- Fully portable, runnable as a single `.jar` file  

---

## 📦 Requirements

- **Java 17** (or a compatible JDK)
- **Maven 3.9+** (for building the project)

Check your setup:
```bash
java -version
mvn -v
```

---

## ⚙️ Installation & Build

### 1️⃣ Clone the repository
```bash
git clone https://github.com/lucabritten/FIT-Combiner-Tool.git
cd FIT_Combiner
```

### 2️⃣ Build with Maven
```bash
mvn clean package
```

This will generate a runnable JAR file inside the `target/` directory:

```
target/fit-combiner.jar
```

---

## ▶️ Usage

Place the `.fit` files you want to combine into the **same folder** as the `fit-combiner.jar` file.  
Then, you can run the program in one of two ways:

### 🖱 Option 1 — Double-click (easiest)
Simply **double-click the `fit-combiner.jar` file**.  
If two `.fit` files are present in the same folder, the program will automatically detect and combine them into:
```
combined.fit
```

### 💻 Option 2 — Command line
Alternatively, you can specify the files explicitly:
```bash
java -jar fit-combiner.jar file1.fit file2.fit
```

---

## 🗂️ Project Structure

```
FIT_Combiner/
├── pom.xml                 # Maven build configuration
├── src/
│   ├── main/java/com/fitcombiner/
│   │   ├── Main.java       # Entry point
│   │   ├── io/             # File input/output handling
│   │   ├── model/          # Data models (e.g. FitFile)
│   │   ├── service/        # Processing logic
│   │   └── util/           # Helper utilities
└── target/
    └── fit-combiner.jar    # Generated runnable JAR
```

---

## 🧩 Technology Stack

- **Java 17**
- **Maven** – Build & Dependency Management  
- **Lombok** – For cleaner and more concise code  
- **Garmin FIT SDK** – For reading and writing FIT files  

---

## 🧑‍💻 Development

To compile the code after making changes:

```bash
mvn clean compile
```

To run the tool locally:

```bash
java -jar target/fit-combiner.jar
```

---

## 🤝 Author

**Luca Britten**  
