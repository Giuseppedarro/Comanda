package dev.giuseppedarro.comanda.features.menu.api

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuUseCase
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MenuResponse(val status: String, val message: String)

fun Route.menuRoutes(
    getMenuUseCase: GetMenuUseCase,
    updateMenuUseCase: UpdateMenuUseCase,
    addCategoryUseCase: AddCategoryUseCase,
    updateCategoryUseCase: UpdateCategoryUseCase,
    deleteCategoryUseCase: DeleteCategoryUseCase,
    getCategoryUseCase: GetCategoryUseCase,
    addItemUseCase: AddItemUseCase,
    updateItemUseCase: UpdateItemUseCase,
    deleteItemUseCase: DeleteItemUseCase,
    getItemUseCase: GetItemUseCase
) {
    authenticate("auth-jwt") {
        route("/menu") {
            // Existing endpoints
            get {
                val menu = getMenuUseCase()
                val menuDto = menu.map { it.toDto() }
                call.respond(HttpStatusCode.OK, menuDto)
            }

            post {
                val request = call.receive<MenuUpdateRequest>()

                // Basic validation
                val validationError = when {
                    request.categories.isEmpty() -> "Menu must contain at least one category"
                    else -> null
                }

                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, MenuResponse("error", validationError))
                    return@post
                }

                val result = updateMenuUseCase(request.categories)

                result.fold(
                    onSuccess = {
                        call.respond(HttpStatusCode.OK, MenuResponse("success", "Menu updated successfully"))
                    },
                    onFailure = { exception ->
                        val status = when (exception) {
                            is IllegalArgumentException -> HttpStatusCode.BadRequest
                            else -> HttpStatusCode.InternalServerError
                        }
                        call.respond(
                            status,
                            MenuResponse("error", exception.message ?: "An unknown error occurred")
                        )
                    }
                )
            }

            // Category CRUD
            route("/categories") {
                post {
                    val request = call.receive<CreateCategoryRequest>()
                    val newCategory = MenuCategory(
                        id = UUID.randomUUID().toString(),
                        name = request.name,
                        items = emptyList()
                    )
                    
                    addCategoryUseCase(newCategory).fold(
                        onSuccess = {
                            call.respond(HttpStatusCode.Created, MenuResponse("success", "Category created"))
                        },
                        onFailure = { e ->
                            call.respond(HttpStatusCode.BadRequest, MenuResponse("error", e.message ?: "Error creating category"))
                        }
                    )
                }

                route("/{id}") {
                    get {
                        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                        getCategoryUseCase(id).fold(
                            onSuccess = { category ->
                                call.respond(HttpStatusCode.OK, category.toDto())
                            },
                            onFailure = {
                                call.respond(HttpStatusCode.NotFound, MenuResponse("error", "Category not found"))
                            }
                        )
                    }

                    put {
                        val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val request = call.receive<UpdateCategoryRequest>()
                        
                        // We need to fetch the existing category to preserve items or handle it in repo
                        // For simplicity, we construct a category object with the ID and new data
                        val categoryToUpdate = MenuCategory(
                            id = id,
                            name = request.name,
                            items = emptyList() // Items are not updated here
                        )

                        updateCategoryUseCase(categoryToUpdate).fold(
                            onSuccess = {
                                call.respond(HttpStatusCode.OK, MenuResponse("success", "Category updated"))
                            },
                            onFailure = { e ->
                                call.respond(HttpStatusCode.InternalServerError, MenuResponse("error", e.message ?: "Error updating category"))
                            }
                        )
                    }

                    delete {
                        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                        deleteCategoryUseCase(id).fold(
                            onSuccess = {
                                call.respond(HttpStatusCode.OK, MenuResponse("success", "Category deleted"))
                            },
                            onFailure = { e ->
                                call.respond(HttpStatusCode.InternalServerError, MenuResponse("error", e.message ?: "Error deleting category"))
                            }
                        )
                    }
                    
                    // Items in Category
                    route("/items") {
                        post {
                            val categoryId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                            val request = call.receive<CreateItemRequest>()
                            
                            val newItem = MenuItem(
                                id = UUID.randomUUID().toString(),
                                categoryId = categoryId,
                                name = request.name,
                                price = request.price,
                                description = request.description,
                                isAvailable = request.isAvailable
                            )

                            addItemUseCase(newItem).fold(
                                onSuccess = {
                                    call.respond(HttpStatusCode.Created, MenuResponse("success", "Item added"))
                                },
                                onFailure = { e ->
                                    call.respond(HttpStatusCode.BadRequest, MenuResponse("error", e.message ?: "Error adding item"))
                                }
                            )
                        }
                    }
                }
            }

            // Item CRUD (Direct access or updates)
            route("/items/{id}") {
                get {
                    val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                    getItemUseCase(id).fold(
                        onSuccess = { item ->
                            call.respond(HttpStatusCode.OK, item.toDto())
                        },
                        onFailure = {
                            call.respond(HttpStatusCode.NotFound, MenuResponse("error", "Item not found"))
                        }
                    )
                }

                put {
                    val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val request = call.receive<UpdateItemRequest>()
                    
                    val itemToUpdate = MenuItem(
                        id = id,
                        categoryId = request.categoryId,
                        name = request.name,
                        price = request.price,
                        description = request.description,
                        isAvailable = request.isAvailable
                    )

                    updateItemUseCase(itemToUpdate).fold(
                        onSuccess = {
                            call.respond(HttpStatusCode.OK, MenuResponse("success", "Item updated"))
                        },
                        onFailure = { e ->
                            call.respond(HttpStatusCode.InternalServerError, MenuResponse("error", e.message ?: "Error updating item"))
                        }
                    )
                }

                delete {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    deleteItemUseCase(id).fold(
                        onSuccess = {
                            call.respond(HttpStatusCode.OK, MenuResponse("success", "Item deleted"))
                        },
                        onFailure = { e ->
                            call.respond(HttpStatusCode.InternalServerError, MenuResponse("error", e.message ?: "Error deleting item"))
                        }
                    )
                }
            }
        }
    }
}