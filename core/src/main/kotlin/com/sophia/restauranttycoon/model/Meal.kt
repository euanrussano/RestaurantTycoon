package com.sophia.restauranttycoon.model

class Meal(
    val name: String,
    val basePrice: Int,
    val cookingTime: Int,
    val eatingTime: Int
) {

    var price: Int = basePrice

}
