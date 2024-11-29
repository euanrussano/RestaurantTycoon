package com.sophia.restauranttycoon.model.state.waiter

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.Vector2
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Seat
import com.sophia.restauranttycoon.model.state.customer.PlacingOrderState

class WaiterWalkingToCustomerState(val seat: Seat) :
    State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        // move always north of the seat
        entity.targetPosition.set(seat.position.x, seat.position.y+1)
    }

    override fun update(entity: RestaurantCharacter) {

        if (entity.position.x == seat.position.x && entity.position.y == seat.position.y+1) {
            val customer = seat.customer?: return
            if (customer.stateMachine.currentState is PlacingOrderState){
                entity.stateMachine.changeState(WaiterTakingOrderState(seat))
                return
            }
        }
    }

    override fun exit(p0: RestaurantCharacter?) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(p0: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Waiter: Walking to customer"
    }

}
