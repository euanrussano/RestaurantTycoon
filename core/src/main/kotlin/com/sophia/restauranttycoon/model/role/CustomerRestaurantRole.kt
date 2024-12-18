package com.sophia.restauranttycoon.model.role

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.fsm.StateMachine
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.Restaurant
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.state.CustomerState
import com.sophia.restauranttycoon.model.state.HungerState

class CustomerRestaurantRole : RestaurantRole, Poolable {

    override val hasHunger: Boolean = true
    var eatingTime: Float = 0f
    var orderToPlace: MealOrder? = null
    var orderPlaced = false

    // stats
    var waitingTimeInQueue = 0f
    var waitingTimeToEat = 0f
    


    override val startingState: State<RestaurantCharacter> = CustomerState.JUST_SPAWNED

    override fun update(delta: Float, restaurantCharacter: RestaurantCharacter, restaurant: Restaurant) {

    }

    override fun reset() {
        eatingTime = 0f
        orderPlaced = false
        orderToPlace = null
    }
}
