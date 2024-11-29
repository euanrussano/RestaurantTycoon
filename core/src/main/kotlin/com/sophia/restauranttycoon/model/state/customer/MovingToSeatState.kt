package com.sophia.restauranttycoon.model.state.customer

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.RestaurantCharacter

class MovingToSeatState : State<RestaurantCharacter> {
    override fun enter(p0: RestaurantCharacter?) {

    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        if (entity.hasTargetPosition()) return

        // check if it has reached its seat
        val seat = restaurant.getAssignedSeatForCustomer(entity)
        if (seat.position.dst(entity.position.x, entity.position.y) == 0f) {
            // it has reached its seat
            entity.stateMachine.changeState(PlacingOrderState())
            return
        }

    }

    override fun exit(p0: RestaurantCharacter?) {

    }

    override fun onMessage(p0: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Customer: Moving to Seat"
    }

}
