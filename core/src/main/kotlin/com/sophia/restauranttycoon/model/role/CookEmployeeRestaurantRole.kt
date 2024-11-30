package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.state.cook.IdleCookState

class CookEmployeeRestaurantRole : EmployeeRestaurantRole(100) {

    override val startingState: State<RestaurantCharacter> = IdleCookState()
    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {
        //TODO("Not yet implemented")
    }
}
