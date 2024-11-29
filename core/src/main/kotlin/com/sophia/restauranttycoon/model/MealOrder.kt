package com.sophia.restauranttycoon.model

import com.badlogic.gdx.math.Vector2

class MealOrder(
    val meal: Meal,
    val customer: RestaurantCharacter,
    val seatLoc: Vector2
) {

    var isReady = false

}
