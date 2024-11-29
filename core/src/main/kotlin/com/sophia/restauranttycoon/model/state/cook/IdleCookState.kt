package com.sophia.restauranttycoon.model.state.cook

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.RestaurantCharacter

class IdleCookState: State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // move north of the nearest counter
        val counter = restaurant.findNearestCounter(entity.position)
        entity.targetPosition.set(counter.position.x, counter.position.y+1)
    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // if near (adjacent) to a counter, check if there is meal order and take it
        val counter = restaurant.findNearestCounter(entity.position)
        if (!Utils.isAdjacent(entity.position, counter.position)) return

        val mealOrdersToCook = counter.mealOrdersToCook
        if (mealOrdersToCook.isEmpty()) return
        val order = mealOrdersToCook.first()
        counter.removeMealOrder(order)
        entity.stateMachine.changeState(CookMoveToStoveState(order))
    }

    override fun exit(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Cook: Idle"
    }

}
