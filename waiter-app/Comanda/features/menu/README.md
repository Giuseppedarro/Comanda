# Menu Feature Module

This module provides the functionality for managing the restaurant's menu. It allows users to view, create, edit, and delete menu categories and the items within them.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Data Layer

This layer handles all network interactions for menu management.

- **`remote/MenuApi.kt`**: A Ktor-based API client that defines a full set of CRUD (Create, Read, Update, Delete) operations for menu categories and items. It uses the `authClient` from `:core`.
- **`remote/dto/`**: Contains Data Transfer Objects for all menu operations, and a `DtoExtensions.kt` file with functions to map these DTOs to and from the domain models.
- **`repository/MenuRepositoryImpl.kt`**: The implementation of the `MenuRepository`. It uses the `MenuApi` and the mapping extensions to handle all data operations, wrapping them in a `Result` type for robust error handling.

### Domain Layer

This layer contains the core business logic and models for the feature.

- **`model/`**: Contains the rich business models `MenuCategory` and `MenuItem`.
- **`repository/MenuRepository.kt`**: The interface (contract) defining the full suite of CRUD operations for the menu.
- **`usecase/`**: This package contains a use case for each CRUD operation, providing a clean entry point to the domain layer.

### Presentation Layer

This is the UI layer, built with Jetpack Compose. It follows a stateful/stateless pattern where the "Screen" composable manages state and the "Content" composable is a stateless, previewable function. It is composed of two main screens:

- **`MenuScreen.kt` / `MenuViewModel.kt`**: This screen provides an overview of all menu categories. It displays them in a list and allows the user to navigate to a specific category's detail screen.
- **`CategoryScreen.kt` / `CategoryViewModel.kt`**: This screen displays all the items within a single category. It provides the ability to add, edit, and delete items in that category. It uses `SavedStateHandle` to receive the category name as a navigation argument.
- **`components/`**: This package contains reusable UI components:
    - **`MenuItemDialog.kt`**: A generic, stateless dialog for editing the fields of a menu item.
    - **`AddMenuItemDialog.kt`** and **`EditMenuItemDialog.kt`**: Stateful wrappers around `MenuItemDialog` for adding a new item and editing an existing one, respectively.
- **`navigation/MenusNavGraph.kt`**: Defines the navigation for the feature with two type-safe routes: `Menu` (for the overview) and `Category` (for the detail view, which includes the `categoryName` argument).

## Dependency Injection

- **`di/MenuModule.kt`**: Provides all dependencies for the feature using Koin, including the API, repository, all use cases, and both `MenuViewModel` and `CategoryViewModel`.
