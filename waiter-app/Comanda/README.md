# Comanda Waiter Application

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-blue.svg?logo=jetpackcompose)
![Jetpack Navigation](https://img.shields.io/badge/Jetpack%20Navigation-2.8.0-blue.svg?logo=jetpack)
![Ktor](https://img.shields.io/badge/Ktor-2.3.12-blue.svg?logo=ktor)
![Koin](https://img.shields.io/badge/Koin-4.1.1-blue.svg)
![Android Gradle Plugin](https://img.shields.io/badge/AGP-8.13.2-brightgreen.svg)

This document provides a complete architectural overview of the **Comanda Waiter Application**, the Android client portion of this repository. The app is designed to help restaurant staff manage tables, orders, and more.

## Architecture

The application is built following modern Android development best practices, with a focus on a modular, scalable, and maintainable architecture. It uses a multi-module approach, with a central `:app` module, a foundational `:core` module, and a series of independent `:features` modules.

### Core Principles

- **Clean Architecture**: Each module is internally organized into `data`, `domain`, and `presentation` layers.
- **Dependency Rule**: The dependency flow is strictly one-way: `app` -> `features` -> `core`. The `:core` module knows nothing about the features, and features know nothing about each other.
- **Dependency Injection**: [Koin](https://insert-koin.io/) is used for dependency injection throughout the app to provide dependencies and manage their lifecycle.
- **UI**: The entire UI is built with [Jetpack Compose](https://developer.android.com/jetpack/compose).
- **Stateful/Stateless UI Pattern**: Feature screens are split into a stateful, non-previewable composable (the "Screen") that manages state and a stateless, easily previewable composable (the "Content") that purely renders the UI.
- **Networking**: Networking is handled by [Ktor](https://ktor.io/), a modern asynchronous HTTP client.
- **Navigation**: Navigation is managed with [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation), using type-safe routes with Kotlin Serialization.

--- 

## Modules

The project is divided into the following modules:

### `:app`

The main application module. Its primary responsibilities are:
- To assemble the final Android application (APK).
- To initialize app-wide components (like Koin) in the `Application` class.
- To host the main `NavHost` and orchestrate the navigation between the different feature modules.
- To observe the user's chosen theme from the `:core` module and apply it globally to the `ComandaTheme` wrapper.

### `:core`

This is the foundational library module for the entire application. It contains shared code, components, and utilities that are used by all feature modules. Its key responsibilities include:

- **Networking**: Providing a robust, auto-refreshing Ktor `HttpClient` (`authClient`) for authenticated calls and a separate `basicClient` for unauthenticated calls.
- **Data Persistence**: Securely persisting authentication tokens, user theme preferences and user language preferences using Jetpack `DataStore` and `AppCompatDelegate`.
- **Domain Logic**: Defining contracts (`TokenRepository`, `ThemeRepository`, `LanguageRepository`) and shared business logic (`LogoutUseCase`, `GetThemePreferencesUseCase`, `SaveThemePreferencesUseCase`, `GetLanguageUseCase`, `SetLanguageUseCase`).
- **Shared UI**: Providing the application's visual identity (`ComandaTheme`), shared components like `ComandaTopAppBar`, and utility functions for price formatting.
- **String Resources**: Contains all shared string resources for the application, with translations for Italian and Dutch.

### `:features`

This directory contains all the independent feature modules. Each feature is a self-contained vertical slice of functionality.

- **`:features:login`**: Responsible for user authentication. Allows users to log in and configure the server's base URL.

- **`:features:tables`**: Provides the main dashboard of the application, displaying a grid of all restaurant tables. It allows users to see the status of each table, add new tables, and initiate the process of taking an order.

- **`:features:orders`**: Handles the creation and management of customer orders. It allows users to view the menu, add items to a table's order, modify quantities, and send the order to the kitchen or print a bill.

- **`:features:menu`**: Provides the functionality for managing the restaurant's menu. It allows authorized users to view, create, edit, and delete menu categories and the items within them.

- **`:features:printers`**: Provides the UI and logic for managing the restaurant's printers, including full CRUD functionality.

- **`:features:settings`**: Contains the main settings screen for the app.
    - **Theme Management**: A sub-feature allowing the user to choose between a light theme, a dark theme, or following the system's theme setting. The choice is persisted on the device.
    - **Language Management**: A sub-feature allowing the user to choose between following the system language or manually selecting a language (English, Italian, or Dutch).
    - **User Management**: A dedicated sub-feature for managing staff accounts, including full CRUD functionality and role management.
