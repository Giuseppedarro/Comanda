# Comanda
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-blue.svg?style=flat&logo=jetpack-compose)
![Navigation](https://img.shields.io/badge/Navigation-2.8.0-blue.svg?style=flat&logo=jetpack-compose)
![Koin](https://img.shields.io/badge/Koin-4.1.1-blue.svg?style=flat&logo=kotlin)
![Android](https://img.shields.io/badge/Android-blue.svg?style=flat&logo=android)
![Material 3](https://img.shields.io/badge/Material%203-BOM-lightgrey.svg?style=flat&logo=material-design)

Comanda is a modern, end-to-end restaurant management system designed to streamline the ordering process. It consists of a mobile app for waiters, a desktop app for cashiers, and a central backend server. This project showcases a complete, multi-platform ecosystem built with the latest Kotlin technologies.

##  Project Goals

- **📱 Waiter App (Android):** Allows waiters to manage tables, take orders, and send them to the kitchen in real-time.
- **💻 Cashier App (Desktop):** Enables cashiers to manage the menu, view incoming orders, and process payments.
- **🌐 Server (Ktor):** A central backend that handles data synchronization, authentication, and business logic using a persistent database.

##  Features

- **User Authentication:** Secure JWT-based authentication for waiters.
- **Modern UI:** A clean and intuitive user interface built with Jetpack Compose and Material 3.
- **Table Management:** View and manage restaurant tables with occupancy status.
- **Order Submission:** Submit orders directly from the waiter app with a categorized menu.
- **Printer Management:** Configure and manage printers (Kitchen, Bar, Cashier) for automatic order ticket printing.
- **Staff Management:** A dedicated administrative module to manage the workforce

##  Technologies & Libraries

This project is built with a modern technology stack, showcasing the power of Kotlin for end-to-end development.

### Waiter App (Android)

| Library | Version |
|---|---|
| Kotlin | 2.0.21 |
| Jetpack Compose BOM | 2024.09.00 |
| Activity Compose | 1.8.0 |
| Lifecycle KTX | 2.6.1 |
| Navigation Compose | 2.8.0 |
| Core KTX | 1.10.1 |
| Material 3 | Managed by BOM |
| Material Icons Extended | 1.6.0 |
| Koin | 4.1.1 |
| Ktor Client | 2.3.12 |
| AndroidX DataStore | 1.1.1 |

### Server (Ktor)

| Library | Version | Description |
|---|---|---|
| Ktor | 3.3.0 | Core framework for building the server. |
| Exposed | 0.41.1 | Kotlin SQL framework for database access. |
| PostgreSQL | 14.5 (DB) / 42.5.0 (Driver) | Database running in Docker (v14.5) with JDBC driver (v42.5.0). |
| HikariCP | 5.0.1 | High-performance JDBC connection pooling. |
| Koin | 3.5.6 | Dependency injection for Ktor. |


##  Architecture

The system is designed with a client-server architecture. The backend is handled by a **Ktor Server**, which manages data and communication.

The client applications (**Waiter App** and **Cashier App**) are built following the **MVVM (Model-View-ViewModel)** architecture pattern, implementing a **UDF (Unidirectional Data Flow)** to ensure a predictable and maintainable state management. **Koin** is used for dependency injection to manage the lifecycle of components like ViewModels and Repositories.

```mermaid
graph TD
    A[Waiter App] --> C{Ktor Server};
    B[Cashier App] --> C;
    C --> D[PostgreSQL Database];
```

##  Project Structure

This project is a monorepo containing the following modules:

- `waiter-app/`: The Android application for waiters.
- `cashier-app/`: The desktop application for the cashier (TODO).
- `server/`: The Ktor backend.

## Getting Started

This guide will help you get the backend server up and running.

### Prerequisites

- **JDK 17** or higher.
- **Docker** and **Docker Compose**. Make sure the Docker daemon is running.

### Running the Backend

The backend consists of the Ktor server and a PostgreSQL database running in Docker.

1.  **Start the Database:**
    Navigate to the `server/` directory in your terminal and run the following command. This will start a PostgreSQL container in the background.
    
    ```sh
    docker compose up -d
    ```

2.  **Run the Server:**
    Open the project in IntelliJ IDEA and run the `Application.kt` file located in `server/src/main/kotlin/dev/giuseppedarro/comanda/`. The server will start on `http://localhost:8080`.

    The first time you run the server, it will automatically create the necessary tables and insert default data:
    - **Default User:**
      - Employee ID: `1234`
      - Password: `password`
    - **Default Printers:** Kitchen, Bar, Cashier (update IPs in production)
    - **Default Tables:** 20 tables numbered 1-20
