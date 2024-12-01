package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Stove
import com.sophia.restauranttycoon.model.state.CookState

class CookEmployeeRestaurantRole : EmployeeRestaurantRole(100){

    override val startingState: State<RestaurantCharacter> = CookState.IDLE
    // assigned meal order
    var order: MealOrder? = null
    // assigned stove
    var stove: Stove? = null
    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {
        //TODO("Not yet implemented")
    }

    override fun reset() {
        order = null
        stove = null
    }
}
