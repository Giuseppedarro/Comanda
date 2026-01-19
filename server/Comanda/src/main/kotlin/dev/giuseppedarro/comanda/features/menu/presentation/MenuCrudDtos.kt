package dev.giuseppedarro.comanda.features.menu.presentation

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val name: String,
    val displayOrder: Int = 0
)

@Serializable
data class UpdateCategoryRequest(
    val name: String,
    val displayOrder: Int = 0
)

@Serializable
data class CreateItemRequest(
    val name: String,
    val price: Double,
    val description: String? = null,
    val isAvailable: Boolean = true,
    val displayOrder: Int = 0
)

@Serializable
data class UpdateItemRequest(
    val categoryId: String,
    val name: String,
    val price: Double,
    val description: String? = null,
    val isAvailable: Boolean = true,
    val displayOrder: Int = 0
)