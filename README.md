### README.md

```markdown
# CryptoForum

## Description
CryptoForum is a Spring Boot application for managing cryptocurrency discussions and forums.

## Technologies
- Java
- Spring Boot
- Gradle
- SQL

## Features
- User authentication and authorization
- CRUD operations for comments and posts
- Filtering and sorting of comments

## Prerequisites
- Java 11 or higher
- Gradle 6.8 or higher
- SQL database (e.g., MySQL, PostgreSQL)

## Installation
Steps to install and run the project locally.

```bash
# Clone the repository
git clone https://github.com/A60Team-CryptoForum/CryptoForum.git

# Navigate to the project directory
cd CryptoForum

# Build and run the project
./gradlew bootRun
```

## Configuration
Description of configuration files and how to set them up.

### `application.properties`
Ensure you have the following properties set in your `application.properties` file:

```properties
database.url=your_database_url
database.username=your_database_username
database.password=your_database_password
```

## Endpoints
List of API endpoints and their methods.

### CommentRestController
- `GET /api/comments/{id}` - Retrieve a comment by ID
- `POST /api/comments` - Create a new comment
- `DELETE /api/comments/{id}` - Delete a comment by ID
- `GET /api/comments` - Retrieve all comments with optional filters
- `PUT /api/comments/{id}` - Update a comment by ID

## Usage
Examples of how to use the API endpoints.

### Retrieve a Comment by ID
```bash
curl -X GET "http://localhost:8080/api/comments/1" -H "accept: application/json"
```

### Create a New Comment
```bash
curl -X POST "http://localhost:8080/api/comments" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"postId\": 1, \"content\": \"This is a comment.\" }"
```

### Delete a Comment by ID
```bash
curl -X DELETE "http://localhost:8080/api/comments/1" -H "accept: application/json"
```

### Retrieve All Comments
```bash
curl -X GET "http://localhost:8080/api/comments" -H "accept: application/json"
```

### Update a Comment by ID
```bash
curl -X PUT "http://localhost:8080/api/comments/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"content\": \"Updated comment.\" }"
```

## Tests
Instructions to run tests.

```bash
# Run tests
./gradlew test
```

## Contributing
If you want to contribute to this project, please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## Authors
- A60Team-CryptoForum - [GitHub Profile](https://github.com/A60Team-CryptoForum)

## License
Information about the project's license.
```