# Reservation Time-Series Exporter

This project provides a service for exporting reservation time-series data to CSV files. It is built with Spring Boot and Java 17, utilizing PostgreSQL for data storage and OpenCSV for handling CSV file generation.

## Features

- **CSV Export**: Provides endpoints to download reservation data in CSV format.
- **Data Formatting**: Supports both total and detailed formats for the CSV output.
- **Conversion**: Handles conversion of values from kW to MW and includes summing of positive and negative values based on query parameters.
- **Dockerized**: The service is containerized using Docker for easy deployment.

## API Endpoints

- `GET /api/v1/flexibility/reservations/{assetId}/market/{marketId}/export`: Exports reservation data for the specified asset and market.

## Components

- **ReservationService**: Handles the business logic for fetching and formatting reservation data.
- **CSV Formatters**: `TotalCSVFormatter` and `DetailedCSVFormatter` handle specific formatting requirements for CSV output.

## Technologies

- **Spring Boot**: Framework for building the service.
- **Java 17**: Programming language used.
- **PostgreSQL**: Database for storing reservation data.
- **OpenCSV**: Library for generating CSV files.
- **Docker**: Containerization of the application.

## Setup

### Prerequisites

- Java 17
- Docker
- Docker Compose

### Installation

1. **Clone the Repository**:

   ```bash
   git clone <repository-url>
   cd <repository-directory>

   ```

2. **Build and Run**:

   ```bash
   ./mvnw clean install
   docker-compose up

   ```

3. **Access the API**:
   The service will be available at http://localhost:8080.

## Testing

- **Unit Tests**: Implemented with JUnit, focusing on testing successful scenarios.
