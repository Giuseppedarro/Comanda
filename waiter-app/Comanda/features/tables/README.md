# Tables Feature Module

This module provides the main dashboard of the application, displaying an overview of all restaurant tables. It allows the user to see the status of each table (Available/Occupied), add new tables, and initiate the process of taking or modifying an order.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Data Layer

Responsible for all data operations.

- **`remote/TableApi.kt`**: A Ktor-based client for the `/tables` API endpoint. It handles fetching all tables and posting new ones. It uses the `authClient` from the `:core` module, so all its requests are automatically authenticated.
- **`remote/dto/TableDto.kt`**: The Data Transfer Object that maps directly to the JSON response from the API. It includes a `toDomain()` extension function to convert the network model into a business model, decoupling the data layer from the domain layer.
- **`repository/TablesRepositoryImpl.kt`**: The concrete implementation of the `TablesRepository`. It calls the `TableApi`, handles errors by emitting an empty list to prevent UI crashes, and maps DTOs to domain models.

### Domain Layer

Contains the core business logic and models for the feature.

- **`model/Table.kt`**: The central business model for this feature. It's a simple data class representing a table with its number and occupation status.
- **`repository/TablesRepository.kt`**: An interface (contract) that defines the data operations for tables, such as `getTables()` which returns a `Flow` of table data, and `addTable()`.
- **`use_case/GetTablesUseCase.kt`**: The use case for retrieving the list of tables.
- **`use_case/AddTableUseCase.kt`**: The use case for adding a new table.

### Presentation Layer

The UI-facing layer, built with Jetpack Compose.

- **`TableOverviewViewModel.kt`**: The ViewModel for the main screen. It manages the `TableOverviewUiState`, handles business logic by calling use cases, provides filtering logic for the table list, and exposes events for one-time actions like navigation.
- **`TableOverviewScreen.kt`**: A sophisticated, reactive screen that serves as the app's main dashboard. It uses a `ModalNavigationDrawer` for top-level navigation, a `LazyVerticalGrid` for displaying tables, `FilterChip`s for filtering, and a `pull-to-refresh` mechanism. It is highly decoupled, delegating all navigation actions to lambda functions.
- **`components/`**: This package contains smaller, reusable UI components that make up the main screen:
    - **`AppDrawer.kt`**: The content for the main navigation drawer.
    - **`TableCard.kt`**: A card that displays a single table, changing its appearance based on the table's status.
    - **`TableDialog.kt`**: An alert dialog to prompt the user for the number of people when opening a new table.
- **`navigation/TablesNavGraph.kt`**: Encapsulates the navigation for this feature. It defines a `tablesGraph` extension function that can be easily plugged into the main app's navigation graph, and it uses lambda parameters to signal navigation requests, keeping the feature decoupled.

## Dependency Injection

- **`di/TablesModule.kt`**: Provides all the necessary dependencies for this feature using Koin, including the API client, repository, use cases, and ViewModel.
