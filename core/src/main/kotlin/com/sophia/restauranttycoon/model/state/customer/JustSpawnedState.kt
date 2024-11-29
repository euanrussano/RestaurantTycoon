package com.sophia.restauranttycoon.model.state.customer

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.RestaurantCharacter

class JustSpawnedState : State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        val (x, y) = entity.restaurant.endOfQueue
        entity.targetPosition.set(x.toFloat(), y.toFloat())
    }

    override fun update(entity: RestaurantCharacter) {
        val (x, y) = entity.restaurant.endOfQueue
        if (entity.position.x == x.toFloat() && entity.position.y == y.toFloat()) {
            entity.stateMachine.changeState(WaitingInQueueState())
        } else if (entity.targetPosition.x != x.toFloat() || entity.targetPosition.y != y.toFloat()) {
            entity.targetPosition.set(x.toFloat(), y.toFloat())
        }
    }

    override fun exit(entity: RestaurantCharacter) {
        entity.clearTargetPosition()
    }

    override fun onMessage(p0: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Customer: Just Spawned"
    }

}
