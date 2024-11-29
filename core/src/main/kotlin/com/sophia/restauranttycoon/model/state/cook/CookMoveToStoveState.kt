package com.sophia.restauranttycoon.model.state.cook

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Stove

class CookMoveToStoveState(val order: MealOrder) :
    State<RestaurantCharacter> {
    override fun enter(entity: RestaurantCharacter) {
        // find the nearest unused stove and move there
        val restaurant = entity.restaurant
        val stove = restaurant.findNearestFreeStove(entity.position)?: return


        entity.targetPosition.set(stove.position.x, stove.position.y-1)
    }

    override fun update(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant

        val stove = restaurant.findNearestFreeStove(entity.position)?: return
        // if near (adjacent) to a stove, check if there is meal order and take it
        if (!Utils.isAdjacent(stove.position, entity.position)) return
        stove.cook = entity

        entity.stateMachine.changeState(CookCookingState(order, stove))
    }

    override fun exit(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Cook: Move to Stove"
    }

}
