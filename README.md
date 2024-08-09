
# BookController Documentation

This document provides an overview of the `BookController` class and its associated components, including the `BookService` and `Book` model.

## Overview

The `BookController` class is a REST controller responsible for managing books in the application. It exposes several endpoints for CRUD operations on `Book` entities.

## Endpoints

### 1. Get All Books

**Endpoint:** `GET /api/books`

**Description:** Retrieves a list of all books.

**Response:**
- **200 OK:** Returns a list of all `Book` objects.

```json
[
    {
        "id": 1,
        "title": "Book Title",
        "author": "Author Name",
        "publicationDate": "01-01-2022",
        "ISBN": "1234567890"
    },
    ...
]
```

### 2. Get Book by ID

**Endpoint:** `GET /api/books/{id}`

**Description:** Retrieves a specific book by its ID.

**Parameters:**
- `id` (path): The ID of the book.

**Response:**
- **200 OK:** Returns the `Book` object if found.
- **400 Bad Request:** If the book is not found.

### 3. Save Book

**Endpoint:** `POST /api/books`

**Description:** Creates a new book.

**Request Body:**
```json
{
    "title": "Book Title",
    "author": "Author Name",
    "publicationDate": "01-01-2022",
    "ISBN": "1234567890"
}
```

**Response:**
- **201 Created:** If the book is successfully created.
- **400 Bad Request:** If the request body is invalid.

### 4. Update Book

**Endpoint:** `PUT /api/books/{id}`

**Description:** Updates an existing book.

**Parameters:**
- `id` (path): The ID of the book.

**Request Body:**
```json
{
    "title": "Updated Title",
    "author": "Updated Author",
    "publicationDate": "02-02-2023",
    "ISBN": "0987654321"
}
```

**Response:**
- **200 OK:** If the book is successfully updated.
- **400 Bad Request:** If the book is not found.

### 5. Delete Book

**Endpoint:** `DELETE /api/books/{id}`

**Description:** Deletes a specific book by its ID.

**Parameters:**
- `id` (path): The ID of the book.

**Response:**
- **200 OK:** If the book is successfully deleted.
- **400 Bad Request:** If the book is not found.

## Exception Handling

### Invalid ID Format

**Exception:** `MethodArgumentTypeMismatchException`

**Description:** Handles cases where the provided ID is not a valid integer.

**Response:**
- **400 Bad Request:** Returns an error message indicating the invalid ID format.

```json
{
    "error": "Invalid ID: abc is not a valid integer."
}
```

## Associated Classes

### 1. Book

The `Book` class is a model representing a book in the application.

```java
@Entity
public class Book {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    @Column
    private String ISBN;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BorrowingRecord> borrowingRecords;
}
```

### 2. BookService

The `BookService` class handles the business logic for managing books.

**Methods:**
- `getAllBooks()`: Retrieves all books.
- `saveBook(BookDto bookDto)`: Saves a new book.
- `getBook(int id)`: Retrieves a book by ID.
- `updateBook(int id, BookDto bookDto)`: Updates an existing book.
- `deleteBook(int id)`: Deletes a book by ID.



# PatronController Documentation

## Overview

The `PatronController` class is a REST controller responsible for handling HTTP requests related to `Patron` entities. It provides endpoints for creating, retrieving, updating, and deleting patrons.

### Base URL

```
/api/patrons
```

## Endpoints

### 1. Get All Patrons

**URL:** `/api/patrons`

**Method:** `GET`

**Description:** Retrieves a list of all patrons.

**Response:**

- **200 OK:** Returns a list of `Patron` objects.

### 2. Get Patron by ID

**URL:** `/api/patrons/{id}`

**Method:** `GET`

**Description:** Retrieves a specific patron by ID.

**Path Variable:**
- `id` (int): The ID of the patron.

**Response:**

- **200 OK:** Returns the `Patron` object.
- **400 Bad Request:** If the patron is not found.

### 3. Delete Patron by ID

**URL:** `/api/patrons/{id}`

**Method:** `DELETE`

**Description:** Deletes a specific patron by ID.

**Path Variable:**
- `id` (int): The ID of the patron to be deleted.

**Response:**

- **200 OK:** If the patron is successfully deleted.
- **400 Bad Request:** If the patron is not found.

### 4. Save Patron

**URL:** `/api/patrons`

**Method:** `POST`

**Description:** Creates a new patron.

**Request Body:**
- `PatronDto`: The DTO containing patron data.

**Response:**

- **200 OK:** Returns the created `Patron` object.

### 5. Update Patron by ID

**URL:** `/api/patrons/{id}`

**Method:** `PUT`

**Description:** Updates a specific patron by ID.

**Path Variable:**
- `id` (int): The ID of the patron to be updated.

**Request Body:**
- `PatronDto`: The DTO containing updated patron data.

**Response:**

- **200 OK:** Returns the updated `Patron` object.
- **400 Bad Request:** If the patron is not found or if the ID is invalid.

## Exception Handling

The `PatronController` also includes an exception handler:

### Handle Type Mismatch

**Exception:** `MethodArgumentTypeMismatchException`

**Description:** Handles exceptions where the ID is not a valid integer.

**Response:**

- **400 Bad Request:** Returns an error message indicating the ID is invalid.

## Dependencies

- `PatronService`: The service class used for handling the business logic related to patrons.

## Example Usage

- **Get All Patrons:** `GET /api/patrons`
- **Get Patron by ID:** `GET /api/patrons/1`
- **Delete Patron by ID:** `DELETE /api/patrons/1`
- **Save Patron:** `POST /api/patrons`
- **Update Patron by ID:** `PUT /api/patrons/1`

# BorrowController Documentation

## Overview

The `BorrowController` class provides RESTful endpoints for managing book borrowing and returning operations in a library system. It allows clients to borrow books, return books, and view borrowing records.

## Endpoints

### 1. Borrow a Book

- **Endpoint:** `POST /api/borrow/{bookId}/patron/{patronId}`
- **Description:** Allows a patron to borrow a book. Requires the book ID and patron ID as path variables and a request body containing borrowing details.
- **Request:**
    - **Path Variables:**
        - `bookId` (int): ID of the book to be borrowed.
        - `patronId` (int): ID of the patron borrowing the book.
    - **Request Body:** `BorrowDto` (contains `borrowingDate` and `returnDate`)
- **Responses:**
    - `201 Created`: Successfully created borrowing record.
    - `400 Bad Request`: Invalid book ID, patron ID, or date issues.

### 2. Return a Book

- **Endpoint:** `PUT /api/borrow/{bookId}/patron/{patronId}`
- **Description:** Allows a patron to return a borrowed book. Requires the book ID and patron ID as path variables and a request body with the return date.
- **Request:**
    - **Path Variables:**
        - `bookId` (int): ID of the book being returned.
        - `patronId` (int): ID of the patron returning the book.
    - **Request Body:** `BorrowDto` (contains `returnDate`)
- **Responses:**
    - `200 OK`: Successfully updated borrowing record.
    - `400 Bad Request`: Invalid book ID, patron ID, or no borrowing record found.

### 3. Get All Borrowing Records

- **Endpoint:** `GET /api/borrow`
- **Description:** Retrieves all borrowing records.
- **Responses:**
    - `200 OK`: Returns a list of all borrowing records.

### 4. Get Borrowed Books by Book ID

- **Endpoint:** `GET /api/borrow/{id}/borrow`
- **Description:** Retrieves borrowing records for a specific book ID.
- **Request:**
    - **Path Variable:**
        - `id` (int): ID of the book.
- **Responses:**
    - `200 OK`: Returns a list of borrowing records for the specified book.
    - `400 Bad Request`: Invalid book ID or no records found.

## Exception Handling

- **Exception:** `DateTimeParseException`
- **Description:** Handles invalid date format exceptions.
- **Response:**
    - `400 Bad Request`: Returns an error message indicating an invalid date format.
