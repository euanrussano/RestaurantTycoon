package com.sophia.restauranttycoon.model.state.customer

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter

class CustomerEatingState(val mealOrder: MealOrder) : State<RestaurantCharacter> {

    var eatingTime = 0f
    override fun enter(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun update(entity: RestaurantCharacter) {
        val delta = GdxAI.getTimepiece().deltaTime
        eatingTime += delta
        if (eatingTime > mealOrder.meal.eatingTime) {
            val restaurant = entity.restaurant
            // pay for the meal
            restaurant.processPaymentForMeal(entity, mealOrder.meal)

            restaurant.releaseCustomer(entity)
            return
        }
    }

    override fun exit(entity: RestaurantCharacter?) {
        // TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter?, p1: Telegram?): Boolean {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "Customer: Eating"
    }

}
