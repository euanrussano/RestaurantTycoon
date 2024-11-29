package com.sophia.restauranttycoon.model.furniture

import com.badlogic.gdx.ai.GdxAI
import com.sophia.restauranttycoon.model.Meal
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter

class Stove(x: Int, y: Int) : Furniture(x, y, 100) {

    var cook: RestaurantCharacter? = null

    private var currentlyCookingMeal: MealOrder? = null
    private var cookingTime = 0f

    fun startCooking(mealOrder: MealOrder) {
        currentlyCookingMeal = mealOrder
        cookingTime = 0f
    }

    fun cookMeal(): Boolean {
        val delta = GdxAI.getTimepiece().deltaTime
        cookingTime += delta
        var isReady = false
        val cookingMeal = currentlyCookingMeal?: return false

        if (cookingTime > cookingMeal.meal.cookingTime) {
            cookingMeal.isReady = true
            currentlyCookingMeal = null
            cookingTime = 0f
            cook = null
            isReady = true
        }
        return isReady
    }
}
