package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.state.CustomerState

class CustomerRestaurantRole : RestaurantRole, Poolable {

    var eatingTime: Float = 0f
    var orderToPlace: MealOrder? = null
    var orderPlaced = false
    override val startingState: State<RestaurantCharacter> = CustomerState.JUST_SPAWNED

    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {

    }

    override fun reset() {
        eatingTime = 0f
        orderPlaced = false
        orderToPlace = null
    }
}
