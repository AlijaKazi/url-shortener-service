# URL Shortener Service

This is a backend application built using Spring Boot and Hibernate, designed to function as a simplified URL shortening service similar to Bitly or TinyURL.

## Objective

The application takes a long URL, generates a short code, stores the mapping in a database, and provides endpoints for redirection and viewing statistics.

## Technologies Used

* **Java 17**
* **Spring Boot 3.2.5** (or the version you used)
* **Spring Data JPA**
* **Hibernate**
* **H2 Database** (in-memory for development)
* **Maven**
* **Lombok** (for boilerplate code reduction)

## Project Structure

The project follows a standard layered architecture:

urlshortener/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── urlshortener/
│   │   │               ├── UrlshortenerApplication.java
│   │   │               ├── controller/
│   │   │               │   └── UrlController.java
│   │   │               ├── model/
│   │   │               │   └── UrlMapping.java
│   │   │               ├── repository/
│   │   │               │   └── UrlRepository.java
│   │   │               └── service/
│   │   │                   └── UrlService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md


## How to Run the Application

1.  **Prerequisites:**
    * Java Development Kit (JDK) 17 or higher installed.
    * Maven (usually comes with Spring Boot setup in IDEs like VS Code).
    * An IDE like VS Code with the "Extension Pack for Java" installed.

2.  **Clone/Download the Project:**
    * If uploading to GitHub, the recruiter will clone it.
    * If submitting a ZIP, extract the contents to your desired directory.

3.  **Navigate to the Project Root:**
    Open your terminal or command prompt and navigate to the `urlshortener` directory (where `pom.xml` is located).

4.  **Build the Project (Optional, but good practice):**
    ```bash
    mvn clean install
    ```
    This command compiles the code and downloads all necessary dependencies.

5.  **Run the Application:**
    You can run the application using the Maven Spring Boot plugin:
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, if using an IDE like VS Code, you can open the `UrlshortenerApplication.java` file and click the "Run" button above the `main` method.

    The application will start on `http://localhost:8080`.

## API Documentation

The application exposes the following REST endpoints:

### 1. Create Short URL

* **Endpoint:** `POST /shorten`
* **Description:** Generates a unique short code for a given long URL and stores the mapping.
* **Request Body (JSON):**
    ```json
    {
      "longUrl": "[https://www.example.com/very/long/link/to/shorten](https://www.example.com/very/long/link/to/shorten)"
    }
    ```
* **Response (JSON - HTTP 200 OK):**
    ```json
    {
      "shortUrl": "http://localhost:8080/YOUR_SHORT_CODE"
    }
    ```
    * `YOUR_SHORT_CODE` will be a randomly generated 6-character alphanumeric string.
* **Example using PowerShell (from VS Code terminal):**
    ```powershell
    Invoke-WebRequest -Uri http://localhost:8080/shorten -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"longUrl": "[https://www.google.com/search?q=spring+boot+url+shortener+example](https://www.google.com/search?q=spring+boot+url+shortener+example)"}' | ConvertFrom-Json
    ```

### 2. Redirect to Original URL

* **Endpoint:** `GET /{shortCode}`
* **Description:** Redirects the client (browser) to the original long URL associated with the provided short code.
* **Response:** HTTP 302 Found with `Location` header pointing to the `longUrl`. Returns HTTP 404 Not Found if the short code is invalid.
* **Example using PowerShell (replace `YOUR_SHORT_CODE`):**
    ```powershell
    # This command will show the redirect header (Location)
    (Invoke-WebRequest -Uri http://localhost:8080/YOUR_SHORT_CODE -MaximumRedirection 0).Headers
    ```
    * *Note: `Invoke-WebRequest` will show "maximum redirection count exceeded" but will display the `Location` header, confirming the redirect.*

### 3. View Stats

* **Endpoint:** `GET /stats/{shortCode}`
* **Description:** Retrieves details about a shortened URL, including its original URL, creation date, and access count.
* **Response (JSON - HTTP 200 OK):**
    ```json
    {
      "id": 1,
      "longUrl": "[https://www.example.com/very/long/link](https://www.example.com/very/long/link)",
      "shortCode": "YOUR_SHORT_CODE",
      "creationDate": "YYYY-MM-DDTHH:MM:SS.NNNNNN",
      "accessCount": 5
    }
    ```
    * Returns HTTP 404 Not Found if the short code is invalid.
* **Example using PowerShell (replace `YOUR_SHORT_CODE`):**
    ```powershell
    Invoke-WebRequest -Uri http://localhost:8080/stats/YOUR_SHORT_CODE | ConvertFrom-Json
    ```

## Bonus Features Implemented (Optional)

* **None implemented in this version.** (You can remove this section or update it if you decide to add any bonus features later).

---
