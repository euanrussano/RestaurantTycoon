package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Counter
import com.sophia.restauranttycoon.model.furniture.Seat
import com.sophia.restauranttycoon.model.state.WaiterState

class WaiterEmployeeRestaurantRole(

) : EmployeeRestaurantRole(100) {

    override val hasHunger: Boolean = false
    var assignedSeat: Seat? = null
    var targetCounter: Counter? = null
    var order: MealOrder? = null

    override val startingState: State<RestaurantCharacter> = WaiterState.IDLE

    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {
//        TODO("Not yet implemented")
    }

    override fun reset() {
        assignedSeat = null
        targetCounter = null
        order = null
    }
}
