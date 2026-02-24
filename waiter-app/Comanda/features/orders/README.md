# Orders Feature Module

This module is responsible for creating and managing customer orders. It allows the user to view the menu, add items to a table's order, modify quantities, and send the final order to the kitchen or print the bill.

## Architecture

This module follows the principles of Clean Architecture, separating the code into `presentation`, `domain`, and `data` layers.

### Data Layer

This layer handles all network interactions for the feature.

- **`remote/OrderApi.kt`**: A Ktor-based API client that defines methods for fetching the menu (`getMenu`), fetching existing orders (`getOrdersForTable`), submitting new orders (`submitOrder`), and printing bills (`printBill`). It uses the `authClient` from `:core` for all requests.
- **`remote/dto/`**: This package contains a comprehensive set of Data Transfer Objects that map directly to the API's JSON structures for requests and responses. The `MenuCategoryDto` and `MenuItemDto` are sourced from the `:core` module.
- **`repository/OrderRepositoryImpl.kt`**: The implementation of the `OrderRepository`. Its key responsibility is to orchestrate data calls and map the flat network DTOs into rich, useful domain models. For example, when fetching an existing order, it makes a second call to the menu API to combine order item data with full menu item details (name, price) into a complete `Order` object.

### Domain Layer

This layer contains the core business logic and models, completely independent of the other layers.

- **`model/`**: This package contains the rich business models for the feature, such as:
    - **`Order`**: Represents a full order, including a type-safe `OrderStatus` enum.
    - **`OrderItem`**: Represents a line item in an order. Crucially, it holds the entire `MenuItem` object, not just an ID, making it easy for the UI to display all necessary information.
    - **`MenuCategory` & `MenuItem`**: Represent the structure of the restaurant's menu.
- **`repository/OrderRepository.kt`**: The interface (contract) defining the data operations required by this feature.
- **`use_case/`**: This package contains simple, focused use cases (`GetMenuUseCase`, `SubmitOrderUseCase`, etc.) that act as the entry point to the domain layer from the presentation layer.

### Presentation Layer

This is the UI layer, built with Jetpack Compose.

- **`MenuOrderViewModel.kt`**: The brain of the feature's UI. It uses `SavedStateHandle` to receive navigation arguments (table number, number of people) in a lifecycle-aware way. It manages the complex `MenuOrderUiState`, including the current list of ordered items, the full menu, and UI visibility states. It contains the business logic for adding/removing items from the virtual cart.
- **`MenuOrderScreen.kt`**: The main screen for this feature. It displays the current order and a grid of menu categories. It uses a `ModalBottomSheet` to allow users to select items from a chosen category. It communicates with the ViewModel to update the order and uses callbacks to notify the UI of the success or failure of network operations.
- **`components/MenuItemRow.kt`**: A reusable composable that displays a single item in the order list and includes controls to change its quantity.
- **`navigation/`**: This package defines the feature's navigation in a modern, type-safe way.
    - **`MenuOrder.kt`**: A `Serializable` data class that defines the navigation route *and* its arguments (`tableNumber`, `numberOfPeople`).
    - **`OrdersNavGraph.kt`**: Defines a nested navigation graph for the feature. It uses the `composable<MenuOrder>` function from Compose Navigation to link the type-safe route to the `MenuOrderScreen` and correctly deserializes the arguments for use by the ViewModel.

## Dependency Injection

- **`di/OrdersModule.kt`**: Provides all dependencies for the feature using Koin. It correctly injects the `authClient` from `:core` into the `OrderApi` and provides the `SavedStateHandle` to the `MenuOrderViewModel`, allowing for type-safe argument passing.
