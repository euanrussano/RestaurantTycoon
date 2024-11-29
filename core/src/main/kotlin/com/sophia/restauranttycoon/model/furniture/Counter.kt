package com.sophia.restauranttycoon.model.furniture

import com.sophia.restauranttycoon.model.MealOrder

class Counter(
    x: Int,
    y: Int
) : Furniture(x, y, 100) {

    private val _mealOrders = mutableListOf<MealOrder>()

    val mealOrdersReady get() = _mealOrders.filter { it.isReady }
    val mealOrdersToCook get() = _mealOrders.filter { !it.isReady }

    fun addOrder(order: MealOrder) {
        _mealOrders += order
    }

    fun removeMealOrder(order: MealOrder) {
        _mealOrders -= order
    }


}
