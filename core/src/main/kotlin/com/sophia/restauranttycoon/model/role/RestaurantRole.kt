package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter

interface RestaurantRole: Poolable {
    val hasHunger: Boolean
    val startingState: State<RestaurantCharacter>


    fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant)

}
