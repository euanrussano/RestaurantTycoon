package com.sophia.restauranttycoon.model.state.waiter

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.RestaurantCharacter

class IdleWaiterState: State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant

        // waiter should wait always near a counter to make easier to grab orders
        val counter = restaurant.findNearestCounter(entity.position)
        entity.targetPosition.set(counter.position.x, counter.position.y-1)
    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // if waiter is still with target position do nothing
        if (entity.hasTargetPosition()) return


        // first check if there is a customer waiting to order
        // find a seat with customer waiting to order
        val customer = restaurant.findFirstCustomerWaitingToOrder()
        // move to seat
        if (customer != null){
            val seat = restaurant.getAssignedSeatForCustomer(customer)
            entity.stateMachine.changeState(WaiterWalkingToCustomerState(seat))
            return
        }

        // otherwise check if there is an order ready to be served
        val counter = restaurant.findNearestCounter(entity.position)
        if (!Utils.isAdjacent(entity.position, counter.position)){
            throw Error("Waiter should be near a counter: waiter at ${entity.position} and counter at ${counter.position}")
        }
        val order = counter.mealOrdersReady.firstOrNull()
        if (order != null){
            counter.removeMealOrder(order)
            entity.stateMachine.changeState(WaiterServingOrderState(order))
            return
        }
    }

    override fun exit(entity: RestaurantCharacter?) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun toString(): String {
        return "Waiter: Idle"
    }

}
