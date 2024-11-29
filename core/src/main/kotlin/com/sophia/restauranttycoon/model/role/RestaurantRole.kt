package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter

interface RestaurantRole {
    val startingState: State<RestaurantCharacter>

    fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant)

}