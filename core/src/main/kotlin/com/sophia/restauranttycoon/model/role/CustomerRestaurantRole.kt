package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.state.customer.JustSpawnedState

class CustomerRestaurantRole : RestaurantRole {

    override val startingState: State<RestaurantCharacter> = JustSpawnedState()

    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {

    }
}
