# Printers Feature Module

This module provides the UI and logic for managing the restaurant's printers. It allows users with the appropriate permissions to view, create, edit, and delete printers that can be used for printing orders and bills.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Data Layer

This layer is responsible for all network interactions related to printers.

- **`remote/PrinterApi.kt`**: A Ktor-based API client that defines a full set of CRUD (Create, Read, Update, Delete) operations for the `/printers` endpoint. It uses the `authClient` from `:core` for all requests.
- **`remote/dto/`**: This package contains the Data Transfer Objects for printer operations: `PrinterDto` for responses, and `CreatePrinterRequest` and `UpdatePrinterRequest` for writing data.
- **`repository/PrinterRepositoryImpl.kt`**: The implementation of the `PrinterRepository`. It calls the `PrinterApi`, wraps all network operations in a `Result` type for robust error handling, and maps between the network DTOs and the domain models.

### Domain Layer

This layer contains the core business logic and models for the feature.

- **`model/Printer.kt`**: The central, `Serializable` business model for this feature. It represents a single printer with its ID, name, IP address, and port.
- **`repository/PrinterRepository.kt`**: The interface (contract) defining the CRUD operations for printers.
- **`use_case/`**: This package contains a use case for each CRUD operation (`GetAllPrintersUseCase`, `CreatePrinterUseCase`, `UpdatePrinterUseCase`, `DeletePrinterUseCase`), providing a clean entry point to the domain layer.

### Presentation Layer

The UI-facing layer, built with Jetpack Compose.

- **`PrinterManagementViewModel.kt`**: The ViewModel for the feature. It manages the `PrinterManagementUiState`, including the list of printers, loading/error states, and the visibility of the add/edit dialogs. After any CUD operation, it reloads the printer list to ensure the UI is always up-to-date.
- **`PrinterManagementScreen.kt`**: The main screen for managing printers. It displays a list of configured printers using a `LazyColumn`, provides a `FloatingActionButton` to add new ones, and uses a pull-to-refresh mechanism. It uses a `Snackbar` to display errors.
- **`components/`**: This package contains the UI building blocks for the screen:
    - **`PrinterListItem.kt`**: A `Card`-based composable that displays a single printer and provides buttons to edit or delete it.
    - **`AddPrinterDialog.kt`**: A dialog with a form for creating a new printer, including input validation.
    - **`EditPrinterDialog.kt`**: A similar dialog for editing an existing printer, pre-filled with the printer's current data.
- **`navigation/PrintersNavGraph.kt`**: Encapsulates the navigation for this feature. It defines a `printersGraph` extension function that links the `Printers` route to the `PrinterManagementScreen` and provides a callback to navigate back.

## Dependency Injection

- **`di/PrintersModule.kt`**: Provides all dependencies for the feature using Koin, including the API client, repository, all use cases, and the `PrinterManagementViewModel`.
