# Orders Feature Module

This module handles the core ordering process, allowing waiters to select items from the menu, manage an order for a specific table, and submit it to the kitchen or print the bill.

## Architecture

This module follows Clean Architecture principles (`presentation`, `domain`, `data`).

### Data Layer

- **`remote/OrderApi.kt`**: Ktor client for order-related endpoints (`/orders`).
- **`repository/OrderRepositoryImpl.kt`**: Implements `OrderRepository`. It orchestrates fetching the menu and existing orders, mapping DTOs to domain models, and handling error mapping (e.g., mapping HTTP 409 to `TableOccupied`).

### Domain Layer

- **`model/`**: Contains `Order`, `OrderItem`, `OrderStatus`, and **`OrderException`**.
- **`repository/OrderRepository.kt`**: Interface for order data operations.
- **`usecase/`**: Use cases for getting the menu, fetching active orders, submitting orders, and printing bills.

### Presentation Layer

- **`OrderErrorMapper.kt`**: Maps `OrderException` and `DomainException` to localized `UiText`.
- **`MenuOrderViewModel.kt`**: Manages the order state, handles item selection, quantity changes, and submission logic. Uses `UiText` for error reporting.
- **`MenuOrderScreen.kt`**: The main UI for taking orders, displaying menu categories and the current order list. Displays localized errors via Toasts.

## Localization

The module is fully localized in **English**, **Italian**, and **Dutch**, including all order-specific status messages and error reports.
