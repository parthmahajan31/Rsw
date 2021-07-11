package com.example.joining.Models

data class GetCategoryModel(
    val categories: List<Category>,
    val status: String
)

data class Category(
    val id: String,
    val name: String
)

data class SaveModel(
    val message: String,
    val status: String
)