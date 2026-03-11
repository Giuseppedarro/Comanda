# Settings Feature Module

This module provides the UI and logic for various application settings. It includes a main settings screen with options for appearance and notifications, as well as a dedicated sub-feature for user management.

## Architecture

This module is composed of a main settings screen and a full-featured user management section that follows the principles of Clean Architecture.

### User Management

The user management section has its own complete `data`, `domain`, and `presentation` layers.

#### Data Layer

- **`remote/UserApi.kt`**: A Ktor-based API client that defines a full set of CRUD operations for the `/users` endpoint. It uses the `authClient` from `:core`.
- **`remote/dto/`**: Contains Data Transfer Objects for all user operations (`UserResponse`, `CreateUserRequest`, `UpdateUserRequest`) and a `UserMapper.kt` file for mapping to the domain model.
- **`repository/UserRepositoryImpl.kt`**: The implementation of the `UserRepository`. It calls `UserApi`, maps DTOs to domain models, and converts technical/network exceptions into domain-level errors (`UserException` and shared `DomainException`) via the core `toDomainException()` pattern.

#### Domain Layer

- **`model/User.kt`**: The clean domain model for a user.
- **`model/UserException.kt`**: Feature-specific domain error hierarchy for user management (e.g., duplicate employee ID, invalid user data, invalid user ID, user not found).
- **`repository/UserRepository.kt`**: The interface (contract) defining the CRUD operations for users.
- **`use_case/`**: This package contains a use case for each CRUD operation (`GetUsersUseCase`, `CreateUserUseCase`, etc.).

### Presentation Layer

This is the UI layer, built with Jetpack Compose. It follows a stateful/stateless pattern where the "Screen" composable manages state and the "Content" composable is a stateless, previewable function.

- **`SettingsScreen.kt`**: The main entry point for the settings feature. It displays a list of setting categories (Appearance, Notifications, Account) and provides navigation to the "Manage Users" and "Theme Settings" screens.
- **`ThemeSettingsScreen.kt` / `ThemeSettingsViewModel.kt`**: A dedicated screen for managing the application's theme. It allows the user to choose between following the system theme or manually selecting light/dark mode. The `ViewModel` interacts with use cases from the `:core` module to persist the user's choice.
- **`LanguageSettingsScreen.kt` / `LanguageSettingsViewModel.kt`**: A dedicated screen for managing the application's language. It allows the user to choose between following the system language or manually selecting a language. The `ViewModel` interacts with use cases from the `:core` module to set the app's language.
- **`ManageUsersScreen.kt` / `ManageUsersViewModel.kt`**: A dedicated screen for managing users. It displays a list of all users and provides functionality to add, edit, and delete them via dialogs. Error and feedback events are exposed as `UiText` so the UI remains localized and independent from data-layer exceptions.
- **`ManageUsersState.kt`**: Defines the UI state for user management, including dialog states and `UiText?` error state.
- **`SettingsErrorMapper.kt`**: Maps `UserException` and shared `DomainException` to localized `UiText` for consistent, feature-level error rendering.
- **`navigation/SettingsNavGraph.kt`**: Defines a nested navigation graph for the feature. It has routes for the main settings screen (`SettingsMainRoute`), the user management screen (`ManageUsersRoute`), the theme settings screen (`ThemeSettingsRoute`), and the language settings screen (`LanguageSettingsRoute`).

## Dependency Injection

- **`di/SettingsModule.kt`**: Provides all dependencies for this feature using Koin. This includes the components for user management (API, repository, use cases, `ManageUsersViewModel`), the `ThemeSettingsViewModel`, and the `LanguageSettingsViewModel`, which resolve their dependencies from the `:core` module.

## Localization

- The module is localized in **English**, **Italian**, and **Dutch** for both UI labels and user-management error/success feedback.
