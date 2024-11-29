package com.sophia.restauranttycoon.model.state.cook

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter

class CookServingState(val order: MealOrder) :
    State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // move to the nearest counter to put the order
        val counter = restaurant.findNearestCounter(entity.position)
        entity.targetPosition.set(counter.position.x, counter.position.y+1)

    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        val counter = restaurant.findNearestCounter(entity.position)
        if (!Utils.isAdjacent(entity.position, counter.position)) return

        counter.addOrder(order)
        entity.stateMachine.changeState(IdleCookState())
    }

    override fun exit(entity: RestaurantCharacter?) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Cook: Serving"
    }

}