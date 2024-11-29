package com.sophia.restauranttycoon.model.state.customer

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.RestaurantCharacter

class WaitingInQueueState : State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {

    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // check if is first of the queue
        if (restaurant.customersInQueueAndIncoming.firstOrNull() == entity){
            // seatch for a free seat
            val freeSeat = restaurant.findFreeSeatIfAny()?: return
            // if there isa free seat, assign it to the customer and target it
            freeSeat.customer = entity
            entity.targetPosition.set(freeSeat.position)
            entity.stateMachine.changeState(MovingToSeatState())
            return
        }


    }

    override fun exit(entity: RestaurantCharacter) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(p0: RestaurantCharacter?, p1: Telegram?): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    override fun toString(): String {
        return "Customer: In Queue"
    }

}
