package com.sophia.restauranttycoon.model.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.MathUtils
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.MealOrder
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.RestaurantTable
import com.sophia.restauranttycoon.model.role.CustomerRestaurantRole
import kotlin.random.asKotlinRandom

enum class CustomerState: State<RestaurantCharacter> {
    JUST_SPAWNED(){
        override fun enter(entity: RestaurantCharacter) {
            val (x, y) = entity.restaurant.endOfQueue
            entity.targetPosition.set(x.toFloat(), y.toFloat())
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as CustomerRestaurantRole
            role.waitingTimeInQueue += GdxAI.getTimepiece().deltaTime

            val (x, y) = entity.restaurant.endOfQueue
            if (entity.position.x == x.toFloat() && entity.position.y == y.toFloat()) {
                entity.stateMachine.changeState(WAITING_IN_QUEUE)
            } else if (entity.targetPosition.x != x.toFloat() || entity.targetPosition.y != y.toFloat()) {
                entity.targetPosition.set(x.toFloat(), y.toFloat())
            }
        }

        override fun exit(entity: RestaurantCharacter) {
            entity.clearTargetPosition()
        }

        override fun toString(): String {
            return "Customer: Just Spawned"
        }
    },
    WAITING_IN_QUEUE(){
        override fun enter(entity: RestaurantCharacter) {
            Gdx.app.log("Customer", "Waiting in queue")
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as CustomerRestaurantRole
            role.waitingTimeInQueue += GdxAI.getTimepiece().deltaTime

            val restaurant = entity.restaurant
            // check if is first of the queue
            if (restaurant.customersInQueueAndIncoming.firstOrNull() == entity){
                // seatch for a free seat
                val freeSeat = restaurant.findFreeSeatIfAny()?: return
                // if there isa free seat, assign it to the customer and target it
                freeSeat.customer = entity
                entity.targetPosition.set(freeSeat.position)
                entity.stateMachine.changeState(MOVING_TO_SEAT)
                // let others know that the customer left the queue
                MessageManager.getInstance().dispatchMessage(entity, Messages.CUSTOMER_LEFT_QUEUE)
                return
            }


        }

        override fun toString(): String {
            return "Customer: In Queue"
        }

        override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
            if (telegram.sender == entity) return false
            if (telegram.message == Messages.CUSTOMER_LEFT_QUEUE){
                entity.stateMachine.changeState(JUST_SPAWNED)
                return true
            }
            return false
        }
    },
    MOVING_TO_SEAT(){
        override fun enter(entity: RestaurantCharacter) {

        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as CustomerRestaurantRole
            role.waitingTimeToEat += GdxAI.getTimepiece().deltaTime

            val restaurant = entity.restaurant
            if (entity.hasTargetPosition()) return

            // check if it has reached its seat
            val seat = restaurant.getAssignedSeatForCustomer(entity)
            if (seat.position.dst(entity.position.x, entity.position.y) == 0f) {
                // it has reached its seat
                entity.stateMachine.changeState(PLACE_ORDER)
                return
            }

        }

        override fun toString(): String {
            return "Customer: Moving to Seat"
        }
    },
    PLACE_ORDER(){
        override fun enter(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant
            // get a random order from the menu
            val meal = restaurant.meals.random(MathUtils.random.asKotlinRandom())

            val role = entity.role as CustomerRestaurantRole
            role.orderToPlace = MealOrder(meal, entity, entity.position)
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as CustomerRestaurantRole
            role.waitingTimeToEat += GdxAI.getTimepiece().deltaTime
        }

        override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
            if (telegram.message == Messages.MEAL_ARRIVED){
                Gdx.app.log("PlacingOrderState", "customer received meal")
                // make sure the customer order is in the correct table and near
                val table = entity.restaurant.findNearest<RestaurantTable>(entity.position)
                if (!Utils.isAdjacent(table.position, entity.position)) {
                    throw Error("Customer should be near a table: customer at ${entity.position} and table at ${table.position}")
                }
                val role = entity.role as CustomerRestaurantRole
                val orderToPlace = role.orderToPlace
                val mealOrder = table.mealOrdersServed.firstOrNull {

                    it.customer == entity && it == orderToPlace
                }
                if (mealOrder == null){
                    throw Error("There is no order in table for customer: customer at ${entity.position} and table at ${table.position}")
                }

                table.mealOrdersServed.remove(mealOrder)
                entity.stateMachine.changeState(EAT)
                return true
            }
            return false
        }

        override fun toString(): String {
            return "Customer: Placing Order"
        }
    },
    EAT{
        override fun enter(entity: RestaurantCharacter) {}

        override fun update(entity: RestaurantCharacter) {
            val delta = GdxAI.getTimepiece().deltaTime

            val role = entity.role as CustomerRestaurantRole
            val mealOrder = role.orderToPlace!!

            role.eatingTime += delta
            if (role.eatingTime > mealOrder.meal.eatingTime) {
                val restaurant = entity.restaurant
                // pay for the meal
                restaurant.processPaymentForMeal(entity, mealOrder.meal)

                restaurant.releaseCustomer(entity)
                return
            }
        }

        override fun toString(): String {
            return "Customer: Eating"
        }
    };
    override fun exit(entity: RestaurantCharacter) {
        //TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
        //TODO("Not yet implemented")
        return false
    }
}
