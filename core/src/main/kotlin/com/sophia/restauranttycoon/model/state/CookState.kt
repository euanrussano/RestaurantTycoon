package com.sophia.restauranttycoon.model.state

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.role.CookEmployeeRestaurantRole

enum class CookState: State<RestaurantCharacter> {
    IDLE(){
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

            val role = entity.role as CookEmployeeRestaurantRole
            role.order = order

            entity.stateMachine.changeState(MOVE_TO_STOVE)
        }

        override fun toString(): String {
            return "Cook: Idle"
        }
    },
    MOVE_TO_STOVE(){
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

            val role = entity.role as CookEmployeeRestaurantRole
            role.stove = stove

            entity.stateMachine.changeState(COOK)
        }

        override fun toString(): String {
            return "Cook: Move to Stove"
        }
    },
    COOK(){
        override fun enter(entity: RestaurantCharacter) {
            val role = entity.role as CookEmployeeRestaurantRole
            val order = role.order!!
            val stove = role.stove!!
            stove.startCooking(order)
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as CookEmployeeRestaurantRole
            val order = role.order!!
            val stove = role.stove!!

            val isReady = stove.cookMeal()
            if (isReady) {
                entity.stateMachine.changeState(SERVING)
                return
            }
        }

        override fun toString(): String {
            return "Cook: Cooking"
        }
    },
    SERVING(){
        override fun enter(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant
            // move to the nearest counter to put the order
            val counter = restaurant.findNearestCounter(entity.position)
            entity.targetPosition.set(counter.position.x, counter.position.y+1)

        }

        override fun update(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant
            val counter = restaurant.findNearestCounter(entity.position)
            if (!Utils.isAdjacent(entity.position, counter.position)) return

            val role = entity.role as CookEmployeeRestaurantRole
            val order = role.order!!
            counter.addOrder(order)
            entity.stateMachine.changeState(IDLE)
        }

        override fun toString(): String {
            return "Cook: Serving"
        }
    };

    override fun exit(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
        return false
    }
}

