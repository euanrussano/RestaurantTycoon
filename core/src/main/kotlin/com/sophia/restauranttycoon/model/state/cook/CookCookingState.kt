package com.sophia.restauranttycoon.model.state.cook

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.Stove

class CookCookingState(val order: MealOrder, val stove: Stove) :
    State<RestaurantCharacter> {

    override fun enter(entity: RestaurantCharacter) {
        stove.startCooking(order)
    }

    override fun update(entity: RestaurantCharacter) {
        val isReady = stove.cookMeal()
        if (isReady) {
            entity.stateMachine.changeState(CookServingState(order))
            return
        }
    }

    override fun exit(entity: RestaurantCharacter?) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Cook: Cooking"
    }

}
