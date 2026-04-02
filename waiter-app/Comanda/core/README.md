# Core Module

This module is the foundational layer of the Comanda application. It contains shared code, components, and utilities that are used by all feature modules. The primary goal of `:core` is to provide a consistent set of tools and standards for building features, while remaining independent of any specific feature's business logic.

## Architecture

The `:core` module is structured by layer, providing a clear separation of concerns for its different responsibilities.

### `network` Package

This package handles all shared client-server communication infrastructure.

- **`HttpClientFactory.kt`**: The heart of the networking layer. It provides two Ktor `HttpClient` instances:
    - **`authClient`**: The primary client for authenticated API calls. It's configured with the `Auth` plugin to automatically add bearer tokens to requests and, crucially, to handle the **automatic refreshing of expired tokens**. This is the client most features will use.
    - **`basicClient`**: A simplified client with no authentication logic. Its specific purpose is to be used for login and for the token refresh process itself, preventing circular dependencies.
- **`BaseUrlProvider.kt`**: A thread-safe holder for the API's base URL, allowing it to be changed dynamically (e.g., from the login screen).
- **`KtorLogger.kt`**: An abstraction for logging to facilitate testing.

### `data` Package

This package is responsible for the secure persistence of shared data.

- **`TokenRepositoryImpl.kt`**: The concrete implementation of `TokenRepository`. It uses a named Jetpack `DataStore` to save tokens to disk.
- **`ThemeRepositoryImpl.kt`**: The concrete implementation of `ThemeRepository`. It uses a separate, named Jetpack `DataStore` to persist the user's chosen theme preferences.
- **`LanguageRepositoryImpl.kt`**: The concrete implementation of `LanguageRepository`. It uses `AppCompatDelegate` to set the app's language.
- **`TinkManager.kt`**: A modern encryption utility using **Google Tink**. It uses `AES-GCM` with keys managed by the `AndroidKeyStore`. It ensures that authentication tokens are always stored securely on the device, never in plain text, and handles the low-level cryptographic details (IV management, Aead) safely.
- **`RepositoryExtensions.kt`**: Shared error-handling utilities for repositories, including `toDomainException()` and `safeApiCall()`, used to convert technical/network exceptions into domain-level errors.

### `domain` Package

This layer defines the contracts and business logic for shared data.

- **`TokenRepository.kt`**: The interface (contract) for storing and retrieving authentication tokens.
- **`ThemeRepository.kt`**: The interface (contract) for storing and retrieving the application's theme preferences.
- **`LanguageRepository.kt`**: The interface (contract) for setting and getting the app's language.
- **`use_case/LogoutUseCase.kt`**: A simple, reusable use case that encapsulates the logic for logging out a user by clearing their stored tokens.
- **`use_case/GetThemePreferencesUseCase.kt` & `SaveThemePreferencesUseCase.kt`**: Use cases for retrieving and saving the app theme preferences.
- **`use_case/GetLanguageUseCase.kt` & `SetLanguageUseCase.kt`**: Use cases for retrieving and saving the app language.
- **`model/DomainException.kt`**: Defines the shared domain error hierarchy (`NetworkException`, `UnauthorizedException`, `ServerException`, `NotFoundException`, `UnknownException`) used across features.

### `ui` & `presentation` Packages

These packages contain the shared visual elements of the application.

- **`ui/theme`**: Contains the Material 3 themes for the app. `ComandaTheme` provides the standard look and feel, while `BrandedTheme` provides a more stylized theme for specific screens like the login page.
- **`presentation/AppBar.kt`**: Provides reusable UI components like `ComandaTopAppBar` and `ComandaBottomBar` to ensure a consistent user experience across different features.
- **`presentation/UiText.kt`**: A shared text abstraction (`StringResource` / `DynamicString`) used to keep ViewModels and mappers UI-framework friendly and localizable.
- **`presentation/ErrorMapper.kt`**: Maps `DomainException` to localized `UiText`, providing a consistent fallback error strategy for all feature modules.

### `utils` Package

Contains miscellaneous helper and utility functions that can be used by any feature.

- **`PriceFormatter.kt`**: Provides convenient extension functions for converting between integer-based prices (cents) and formatted currency strings.

### `di` Package

- **`CoreModule.kt`**: The Koin dependency injection module for `:core`. It correctly constructs and provides all the components listed above, making them available for feature modules to use. It uses named qualifiers (`authClient`, `basicClient`, `tokenDataStore`, `themeDataStore`) to differentiate between the various components.
