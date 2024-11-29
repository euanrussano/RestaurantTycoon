package com.sophia.restauranttycoon.model.state.waiter

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.RestaurantTable

class WaiterServingOrderState(val order: MealOrder) :
    State<RestaurantCharacter> {

    override fun enter(entity: RestaurantCharacter) {
        val loc = order.seatLoc
        entity.targetPosition.set(loc.x, loc.y+1)

    }

    override fun update(entity: RestaurantCharacter) {
        if (entity.hasTargetPosition()) return

        val restaurant = entity.restaurant

        // make sure waiter is near the customer
        if (!Utils.isAdjacent(order.customer.position, entity.position)){
            throw Error("Waiter should be near a customer: waiter at ${entity.position} and customer at ${order.customer.position}")
        }
        // place order on table and let customer know
        val table = restaurant.findNearest<RestaurantTable>(order.customer.position)
        table.mealOrdersServed.add(order)
        MessageManager.getInstance().dispatchMessage(entity, order.customer, Messages.MEAL_ARRIVED)

        entity.stateMachine.changeState(IdleWaiterState())


    }

    override fun exit(entity: RestaurantCharacter?) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Waiter: Serving"
    }

}
