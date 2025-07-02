# 📊 Report Search Service

A Spring Boot microservice for retrieving and managing reports with:

- Search and autocomplete (Elasticsearch)
- Redis caching
- gRPC-based feature flag checking
- AWS SQS messaging
- JWT-based stateless authentication
- REST API with DTO mapping (MapStruct)
- Global exception handling
- Integration and unit tests with Testcontainers and Mockito

## 🚀 Features

- Full-text and prefix search on reports via Elasticsearch
- Redis caching with TTL and custom serialization
- Feature flag enforcement via gRPC with @CheckFeatures annotation
- Report activity notification via Amazon SQS
- JWT authentication filter validating tokens on each request
- REST API endpoints for report fetching and searching
- DTO conversion between domain and API models using MapStruct
- Exception handling with custom exceptions and controller advice
- Integration tests using Testcontainers (Elasticsearch, LocalStack)
- Unit tests with Mockito for service and controller layers
- OpenAPI 3.0 specification

## Tech Stack

- Spring Boot
- Redis
- Elasticsearch
- Amazon SQS
- gRPC
- JWT
- MapStruct
- Testcontainers
- Mockito
- Lombok
- Jackson
- OpenAPI 3.0

## Example of .env file

```
SQS_ACCESS_KEY=your_sqs_access_key
SQS_SECRET_KEY=your_sqs_secret_key
SQS_ENDPOINT=your_sqs_endpoint
SQS_QUEUE_NAME=your_sqs_queue_name
JWT_SECRET=86400000
```

## Running the Application with Gradle
```
./gradlew clean build
./gradlew bootRun
```

## Run Tests with Gradle
```
./gradlew test
```
