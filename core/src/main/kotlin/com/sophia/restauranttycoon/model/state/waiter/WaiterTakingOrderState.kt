package com.sophia.restauranttycoon.model.state.waiter

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.Vector2
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Seat
import com.sophia.restauranttycoon.model.state.customer.PlacingOrderState

class WaiterTakingOrderState(val seat: Seat) :
    State<RestaurantCharacter> {
    override fun enter(p0: RestaurantCharacter?) {
        //TODO("Not yet implemented")
    }

    override fun update(entity: RestaurantCharacter) {
        val customer = seat.customer?: return
        val state = customer.stateMachine.currentState as? PlacingOrderState ?: return
        if (state.orderPlaced) return

        // take the order from customer
        val order = state.orderToPlace?: return
        state.orderPlaced = true
        entity.stateMachine.changeState(SendOrderToKitchenState(order))
        return

    }

    override fun exit(p0: RestaurantCharacter?) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(p0: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Waiter: Taking Order"
    }

}
