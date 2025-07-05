# PDF and Image Merger

A Java application that merges PDF files and image files (JPEG, PNG, JPG) into a single PDF document.

## Features

- Merges multiple PDF files into one
- Converts and adds image files (JPEG, PNG, JPG) to the PDF
- Maintains image quality and aspect ratios
- Processes files in alphabetical order
- Handles errors gracefully (continues processing even if one file fails)

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone or download this repository
2. Navigate to the project directory
3. Build the project using Maven:

```bash
mvn clean compile
```

## Usage

### Prerequisites

1. Place your PDF and image files in the `files/` folder at the root of the project
2. Supported file formats:
   - PDF files (`.pdf`)
   - JPEG images (`.jpg`, `.jpeg`)
   - PNG images (`.png`)

### Running the Application

#### Option 1: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.mergefiles.Main"
```

#### Option 2: Build and Run JAR
```bash
# Build the JAR file
mvn clean package

# Run the JAR file
java -jar target/pdf-image-merger-1.0.0.jar
```

### Output

The application will:
1. Scan the `files/` folder for supported files
2. Process them in alphabetical order
3. Create a merged PDF file named `merged_output.pdf` in the project root
4. Display progress information in the console

## Example

If you have the following files in the `files/` folder:
- `documentos da fé cristã.pdf`
- `Catalogo ISOBRAS.pdf`
- `Dream Backyard Pool.jpeg`
- `210743-OZX4O9-914.jpg`

The application will:
1. Process the PDF files first (in alphabetical order)
2. Then process the image files
3. Create a single PDF containing all pages from the PDFs and all images as separate pages

## Error Handling

- If a file cannot be processed, the application will log an error and continue with the remaining files
- The final PDF will contain all successfully processed files
- Check the console output for any error messages

## Dependencies

This project uses the following main dependencies:
- **iText 7**: For PDF manipulation and creation
- **Maven**: For build management and dependency resolution

## License

This project is open source and available under the MIT License. 