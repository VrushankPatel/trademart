# ⚠️ DEPRECATED ⚠️

**This repository is no longer maintained or supported. Please refer to [Tradestar](https://github.com/VrushankPatel/tradestar) for the latest version of this project. All features and functionality have been migrated to Tradestar.**

---

# TradeMart Trading Gateway Simulator

[![deprecated](https://img.shields.io/badge/Stability-Deprecated-red)](http://github.com/badges/stability-badges)
[![No Maintenance Intended](https://unmaintained.tech/badge.svg)](http://unmaintained.tech/)
![Java](https://img.shields.io/badge/Java-17-red.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

TradeMart is a comprehensive trading gateway simulator that supports multiple market protocols including FIX, OUCH, and ITCH. It provides a robust platform for testing and simulating trading environments with realistic market data.

## Features

- **Multi-Protocol Support**: Simulates trading environments using industry-standard protocols
  - FIX (Financial Information eXchange) Protocol
  - OUCH (Order Entry) Protocol
  - ITCH (Market Data) Protocol
- **Real-time Market Data**: Simulated market data updates for popular stocks
- **Order Management**: Create, modify, and cancel orders through various protocols
- **User Authentication**: Secure JWT-based authentication system
- **Database Integration**: H2 in-memory database for development, PostgreSQL for production
- **API Documentation**: Swagger UI for API exploration
- **WebSocket Support**: Real-time updates via WebSocket connections

## Architecture

The application is built with a microservices-inspired architecture:

- **Protocol Handlers**: Dedicated handlers for each trading protocol
- **Order Service**: Central service for order management
- **Market Data Service**: Simulates market data updates
- **Security Layer**: JWT-based authentication and authorization
- **Database Layer**: JPA repositories for data persistence

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (for production environment)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/trademart.git
   cd trademart
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080 by default.

### Configuration

The application supports multiple profiles:

- **dev**: Development environment with H2 in-memory database
- **qa**: QA environment with separate configuration
- **prod**: Production environment with PostgreSQL database

Configuration is managed through `application.yml` files.

## API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Protocol Support

### FIX Protocol

The application supports FIX 4.4 protocol with the following features:
- New Order Single messages
- Execution Reports
- Order Cancel Requests
- Order Status Requests

Configuration is managed through `FIX44.xml` and application properties.

### OUCH Protocol

OUCH protocol support includes:
- Order entry
- Order modification
- Order cancellation
- Order status queries

### ITCH Protocol

ITCH protocol is used for market data dissemination:
- Real-time price updates
- Order book snapshots
- Trade reports

## Database Schema

The application uses the following database schema:

- **Users**: Stores user information and authentication details
- **Orders**: Stores order information including status, price, and quantity

## Security

The application implements JWT-based authentication:
- Secure token generation and validation
- Role-based access control
- Password encryption

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Vrushank Patel**

## Acknowledgments

- QuickFIX/J for FIX protocol implementation
- Spring Boot team for the excellent framework
- All contributors who have helped shape this project 