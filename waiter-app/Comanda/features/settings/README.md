# Settings Feature Module

This module provides the UI and logic for various application settings. It includes a main settings screen with options for appearance and notifications, as well as a dedicated sub-feature for user management.

## Architecture

This module is composed of a main settings screen and a full-featured user management section that follows the principles of Clean Architecture.

### User Management

The user management section has its own complete `data`, `domain`, and `presentation` layers.

#### Data Layer

- **`remote/UserApi.kt`**: A Ktor-based API client that defines a full set of CRUD operations for the `/users` endpoint. It uses the `authClient` from `:core`.
- **`remote/dto/`**: Contains Data Transfer Objects for all user operations (`UserResponse`, `CreateUserRequest`, `UpdateUserRequest`) and a `UserMapper.kt` file for mapping to the domain model.
- **`repository/UserRepositoryImpl.kt`**: The implementation of the `UserRepository`. It calls the `UserApi` and maps the results to and from the domain models.

#### Domain Layer

- **`model/User.kt`**: The clean domain model for a user.
- **`repository/UserRepository.kt`**: The interface (contract) defining the CRUD operations for users.
- **`use_case/`**: This package contains a use case for each CRUD operation (`GetUsersUseCase`, `CreateUserUseCase`, etc.).

### Presentation Layer

- **`SettingsScreen.kt`**: The main entry point for the settings feature. It displays a list of setting categories (Appearance, Notifications, Account) and provides navigation to the "Manage Users" screen. For simple toggles like notifications, it uses its own internal state.
- **`ManageUsersScreen.kt` / `ManageUsersViewModel.kt`**: A dedicated screen for managing users. It displays a list of all users and provides functionality to add, edit, and delete them via dialogs. The ViewModel uses a `Channel` to send one-time UI events like snackbar messages.
- **`ManageUsersState.kt`**: Defines the comprehensive UI state for the user management screen, including separate data classes for the state of the add and edit dialogs.
- **`navigation/SettingsNavGraph.kt`**: Defines a nested navigation graph for the feature. It has a main route (`SettingsMainRoute`) for the `SettingsScreen` and a secondary route (`ManageUsersRoute`) for the `ManageUsersScreen`.

## Dependency Injection

- **`di/SettingsModule.kt`**: Provides all dependencies for the user management part of the feature using Koin, including the API, repository, use cases, and the `ManageUsersViewModel`.
