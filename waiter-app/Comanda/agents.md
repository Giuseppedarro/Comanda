# Backend API proposal for waiter-app (Comanda)

This document proposes REST endpoints for the backend to support the current Android client. It reflects the models and DTOs already present in the app code and suggests request/response payloads. All endpoints are prefixed by the base URL configured in the app (see `BaseUrlProvider`).

## General

- Transport: HTTPS
- Auth: Bearer Token (JWT) in `Authorization: Bearer <token>` header where noted
- Content type: `application/json; charset=utf-8`

## Authentication

- **POST /auth/login**
  - **Request**: `{ "employeeId": string, "password": string }`
  - **Response**: `{ "accessToken": string, "refreshToken"?: string, "expiresIn": number }`
  - **Current Implementation**: The app sends a `LoginRequest` with `employeeId` and `password`. The `LoginRepositoryImpl` handles the API call and saves the tokens upon success.

## Tables

- **GET /tables** (auth)
  - Returns the list of tables and whether they are occupied
  - **Response**:
    ```json
    [
      { "number": 1, "isOccupied": false },
      { "number": 2, "isOccupied": true }
    ]
    ```
  - **Current Implementation**: The `TablesRepositoryImpl` makes a GET request to `/tables` and emits a list of `Table` objects.

- **PATCH /tables/{tableNumber}**
  - **Purpose**: update table state (occupied/free), optionally attach an active order id
  - **Request (one or more fields)**: `{ "isOccupied": boolean, "activeOrderId"?: string | null }`
  - **Response**: `{ "number": number, "isOccupied": boolean, "activeOrderId"?: string | null }`
  - **Current Implementation**: This endpoint is not yet implemented in the app.

## Menu

- **GET /menu** (auth)
  - Returns categories with items to display in the order screen bottom sheet
  - **Response example (aligned with `MenuCategory`/`MenuItem` in app)**:
    ```json
    [
      {
        "name": "Appetizers",
        "items": [
          { "id": "bruschetta", "name": "Bruschetta", "price": 6.50 },
          { "id": "caprese", "name": "Caprese Salad", "price": 7.00 }
        ]
      },
      {
        "name": "Drinks",
        "items": [
          { "id": "cola", "name": "Cola", "price": 2.50 }
        ]
      }
    ]
    ```
  - **Notes**:
    - In the Android code today, `MenuItem.price` is a string, but for the backend use numeric `price` (decimal). The client can format it for display.
  - **Current Implementation**: The `OrderRepositoryImpl` currently uses a mock menu. The `GET /menu` endpoint is not yet implemented.

## Orders

- **GET /orders/{tableNumber}** (auth)
  - **Purpose**: fetch orders (or the active order) for a table
  - **Response (aligned with `GetOrderResponse` and `OrderResponseItem`)**:
    ```json
    [
      {
        "tableNumber": 5,
        "numberOfPeople": 4,
        "items": [
          { "menuItemId": "gourmet_burger", "name": "Gourmet Burger", "price": 12.99, "quantity": 1 },
          { "menuItemId": "cola", "name": "Cola", "price": 2.50, "quantity": 2 }
        ]
      }
    ]
    ```
  - **Current Implementation**: The `OrderRepositoryImpl` makes a GET request to `/orders/{tableNumber}` and maps the response to a list of `OrderItem` objects. It uses the `name` of the `MenuItem` as the `menuItemId`.

- **POST /orders** (auth)
  - **Purpose**: submit an order
  - **Request (aligned with `SubmitOrderRequest` and `OrderItemRequest` in the app)**:
    ```json
    {
      "tableNumber": 5,
      "numberOfPeople": 4,
      "items": [
        { "menuItemId": "gourmet_burger", "quantity": 1 },
        { "menuItemId": "cola", "quantity": 2 }
      ]
    }
    ```
  - **Response**: `{ "orderId": string, "status": "accepted" | "rejected", "message"?: string }`
  - **Current Implementation**: The `OrderRepositoryImpl` makes a POST request to `/orders` with a `SubmitOrderRequest` object. It uses the `name` of the `MenuItem` as the `menuItemId`.

- **PATCH /orders/{orderId}** (auth)
  - **Purpose**: update order status (e.g., sent, preparing, served, closed)
  - **Request**: `{ "status": "sent" | "preparing" | "served" | "closed" }`
  - **Response**: `{ "orderId": string, "status": string }`
  - **Current Implementation**: This endpoint is not yet implemented in the app.

## Notes on IDs and prices

- The app’s `OrderItemRequest` currently uses `menuItemId: string` and assumes it can be the menu item name. Prefer stable IDs from your menu (e.g., "gourmet_burger"). The client can map from name to ID once menu is fetched.
- Use numeric `price` on the backend and let the client format as string.

## Error format

- Recommend a uniform error body:
  ```json
  { "error": { "code": "string", "message": "human readable" } }
  ```

## Security

- Validate that the table exists and is free/occupied consistently
- Authorization: waiters should only see/create orders

## Open questions for backend

- Do you want a concept of “active order per table” vs multiple orders per table? The client can adapt if you provide either a single active order endpoint: `GET /tables/{tableNumber}/active-order`.
- Should sending an order mark the table as occupied automatically? If yes, this can be handled on POST /orders.
- Do you need modifiers/notes for items? If yes, extend `OrderItemRequest` with `notes?: string` and/or `modifiers?: [{ id: string, quantity: number }]`.

## Client navigation behavior (current state)

- After the user clicks the send icon in the order screen, the app shows a toast "Order sent" and navigates back to the Table Overview screen. When the backend is ready, the client will call POST /orders before navigating.
