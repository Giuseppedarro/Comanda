# Menu Feature Module

This module provides the functionality for managing the restaurant's menu. It allows users to view, create, edit, and delete menu categories and the items within them.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Data Layer

This layer handles all network interactions for menu management.

- **`remote/MenuApi.kt`**: A Ktor-based API client that defines a full set of CRUD (Create, Read, Update, Delete) operations for menu categories and items. It uses the `authClient` from `:core`.
- **`remote/dto/`**: Contains Data Transfer Objects for all menu operations, and a `DtoExtensions.kt` file with functions to map these DTOs to and from the domain models.
- **`repository/MenuRepositoryImpl.kt`**: The implementation of the `MenuRepository`. It uses the `MenuApi` and mapping extensions to handle data operations. It maps technical errors (like HTTP 409 Conflict) to domain-specific `MenuException`s for better error reporting.

### Domain Layer

This layer contains the core business logic and models for the feature.

- **`model/`**: Contains business models `MenuCategory`, `MenuItem`, and **`MenuException`**. The latter defines specific menu errors such as `DuplicateName` or `CategoryNotEmpty`.
- **`repository/MenuRepository.kt`**: The interface (contract) defining the suite of CRUD operations for the menu.
- **`usecase/`**: Contains a use case for each CRUD operation, providing a clean entry point to the domain layer.

### Presentation Layer

This is the UI layer, built with Jetpack Compose.

- **`MenuErrorMapper.kt`**: A feature-specific mapper that converts `MenuException` and standard `DomainException` into localized `UiText` objects.
- **`MenuViewModel.kt` / `CategoryViewModel.kt`**: Manage UI state using `UiText?` for error reporting. They use the `toMenuUiText()` extension to handle exceptions from the domain layer.
- **`MenuScreen.kt` / `CategoryScreen.kt`**: Display localized error messages. `CategoryScreen` uses a `SnackbarHost` to show transient errors during menu management actions.
- **`components/`**: Contains reusable UI components like `MenuItemDialog`, `AddMenuItemDialog`, and `EditMenuItemDialog`.
- **`navigation/MenusNavGraph.kt`**: Defines type-safe navigation for the feature.

## Dependency Injection

- **`di/MenuModule.kt`**: Provides all dependencies for the feature using Koin, resolving core dependencies like `HttpClient` (`authClient`).

## Localization

The module is fully localized in **English**, **Italian**, and **Dutch**, including all menu-specific error messages.
