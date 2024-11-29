package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.state.waiter.IdleWaiterState

class WaiterEmployeeRestaurantRole : EmployeeRestaurantRole() {

    override val startingState: State<RestaurantCharacter> = IdleWaiterState()

    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {
//        TODO("Not yet implemented")
    }
}
