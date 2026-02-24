# Login Feature Module

This module is responsible for user authentication. It allows users to log in with an employee ID and password. It also provides functionality to configure the server's base URL.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Presentation Layer

The presentation layer is responsible for the UI and handling user interactions.

- **`LoginViewModel.kt`**: Manages the UI state (`LoginUiState`) for the login screen. It interacts with the domain layer's use cases to perform login operations and manage the base URL.
- **`LoginScreen.kt`**: A Jetpack Compose screen that displays the login form, handles user input for employee ID and password, shows loading states, and displays error messages. It also contains a dialog to allow the user to change the server's base URL.
- **`navigation/LoginNavGraph.kt`**: Defines the navigation for this feature. It provides a `loginGraph` extension function for `NavGraphBuilder`, making it a self-contained navigation component that can be easily integrated into the main app's navigation. It communicates a successful login via a callback.

### Domain Layer

The domain layer contains the core business logic of the feature.

- **`repository/LoginRepository.kt`**: An interface that defines the contract for the login functionality.
- **`use_case/LoginUseCase.kt`**: Orchestrates the login process. It performs validation on the user input and calls the `LoginRepository` to authenticate the user.
- **`use_case/GetBaseUrlUseCase.kt`**: A use case to retrieve the current server base URL.
- **`use_case/SetBaseUrlUseCase.kt`**: A use case to sanitize and save a new server base URL.

### Data Layer

The data layer is responsible for data operations, such as making network requests.

- **`remote/LoginApi.kt`**: A Ktor-based API client responsible for making the login request. It uses the `basicClient` from `:core`.
- **`dto` package**: Contains the `LoginRequest` Data Transfer Object used for serializing the network request. The `LoginResponse` is sourced from the `:core` module.
- **`repository/LoginRepositoryImpl.kt`**: The concrete implementation of the `LoginRepository` interface. It uses the `LoginApi` to perform the network request and the `:core` module's `TokenRepository` to store the received tokens.

## Dependency Injection

- **`di/LoginModule.kt`**: Provides all the necessary dependencies for this feature using Koin. It defines the creation of the `LoginApi`, the `LoginRepository`, all use cases, and the `LoginViewModel`. It resolves dependencies from the `:core` module, such as the `HttpClient` (`basicClient`) and `TokenRepository`.
