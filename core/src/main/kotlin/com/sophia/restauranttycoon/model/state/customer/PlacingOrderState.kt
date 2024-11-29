package com.sophia.restauranttycoon.model.state.customer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.MathUtils
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.RestaurantTable
import kotlin.random.asKotlinRandom

class PlacingOrderState : State<RestaurantCharacter> {
    var orderPlaced = false
    var orderToPlace: MealOrder? = null

    override fun enter(entity: RestaurantCharacter) {
        val restaurant = entity.restaurant
        // get a random order from the menu
        val meal = restaurant.meals.random(MathUtils.random.asKotlinRandom())
        orderToPlace = MealOrder(meal, entity, entity.position)
    }

    override fun update(entity: RestaurantCharacter?) {

    }

    override fun exit(entity: RestaurantCharacter?) {

    }

    override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
        if (telegram.message == Messages.MEAL_ARRIVED){
            Gdx.app.log("PlacingOrderState", "customer received meal")
            // make sure the customer order is in the correct table and near
            val table = entity.restaurant.findNearest<RestaurantTable>(entity.position)
            if (!Utils.isAdjacent(table.position, entity.position)) {
                throw Error("Customer should be near a table: customer at ${entity.position} and table at ${table.position}")
            }
            val mealOrder = table.mealOrdersServed.firstOrNull {
                it.customer == entity && it == orderToPlace
            }
            if (mealOrder == null){
                throw Error("There is no order in table for customer: customer at ${entity.position} and table at ${table.position}")
            }

            table.mealOrdersServed.remove(mealOrder)
            entity.stateMachine.changeState(CustomerEatingState(mealOrder))
            return true
        }
        return false
    }

    override fun toString(): String {
        return "Customer: Placing Order"
    }

}
