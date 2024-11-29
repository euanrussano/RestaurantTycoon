package com.sophia.restauranttycoon.model.furniture

import com.sophia.restauranttycoon.model.MealOrder

class RestaurantTable(x: Int, y: Int): Furniture(x, y, 100) {

    val mealOrdersServed = mutableListOf<MealOrder>()
}
