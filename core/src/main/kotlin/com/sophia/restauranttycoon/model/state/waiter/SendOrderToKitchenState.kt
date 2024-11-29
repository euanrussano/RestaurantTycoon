package com.sophia.restauranttycoon.model.state.waiter

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Counter

class SendOrderToKitchenState(val order: MealOrder) :
    State<RestaurantCharacter> {


    private var targetCounter: Counter? = null

    override fun enter(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // move to the counter and place the order
        val counter = restaurant.findNearestCounter(entity.position)?: return
        targetCounter = counter
        entity.targetPosition.set(counter.position.x, counter.position.y-1)

    }

    override fun update(entity: RestaurantCharacter) {
        val counter = targetCounter!!
        if (entity.position.x == counter.position.x && entity.position.y == counter.position.y-1) {
            counter.addOrder(order)
            entity.stateMachine.changeState(IdleWaiterState())
            return
        }
    }

    override fun exit(entity: RestaurantCharacter) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Waiter: Order to Kitchen"
    }

}
