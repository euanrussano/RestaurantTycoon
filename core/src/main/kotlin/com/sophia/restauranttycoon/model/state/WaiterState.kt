package com.sophia.restauranttycoon.model.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.Utils
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.RestaurantTable
import com.sophia.restauranttycoon.model.role.CustomerRestaurantRole
import com.sophia.restauranttycoon.model.role.WaiterEmployeeRestaurantRole

enum class WaiterState: State<RestaurantCharacter> {
    IDLE{
        override fun enter(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant

            // waiter should wait always near a counter to make easier to grab orders
            val counter = restaurant.findNearestCounter(entity.position)
            entity.targetPosition.set(counter.position.x, counter.position.y-1)
        }

        override fun update(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant
            // if waiter is still with target position do nothing
            if (entity.hasTargetPosition()) return


            // first check if there is a customer waiting to order
            // find a seat with customer waiting to order
            val customer = restaurant.customersWaitingToOrder.firstOrNull()
            // move to seat
            if (customer != null){
                val seat = restaurant.getAssignedSeatForCustomer(customer)
                val role = entity.role as WaiterEmployeeRestaurantRole
                role.assignedSeat = seat
                entity.stateMachine.changeState(WALK_TO_CUSTOMER)
                return
            }

            // otherwise check if there is an order ready to be served
            val counter = restaurant.findNearestCounter(entity.position)
            if (!Utils.isAdjacent(entity.position, counter.position)){
                throw Error("Waiter should be near a counter: waiter at ${entity.position} and counter at ${counter.position}")
            }
            val order = counter.mealOrdersReady.firstOrNull()
            if (order != null){
                counter.removeMealOrder(order)
                val role = entity.role as WaiterEmployeeRestaurantRole
                role.order = order
                entity.stateMachine.changeState(SERVE_ORDER)
                return
            }
        }

        override fun toString(): String {
            return "Waiter: Idle"
        }
    },
    WALK_TO_CUSTOMER{
        override fun enter(entity: RestaurantCharacter) {
            // move always north of the seat
            val role = entity.role as WaiterEmployeeRestaurantRole
            val seat = role.assignedSeat!!
            entity.targetPosition.set(seat.position.x, seat.position.y+1)
            Gdx.app.log("Waiter", "Walking to customer")
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as WaiterEmployeeRestaurantRole
            val seat = role.assignedSeat!!
            if (entity.position.x == seat.position.x && entity.position.y == seat.position.y+1) {
                val customer = seat.customer?: return
                if (customer.stateMachine.currentState == CustomerState.PLACE_ORDER){
                    entity.stateMachine.changeState(TAKE_ORDER)
                    return
                }
            }
        }

        override fun toString(): String {
            return "Waiter: Walking to customer"
        }
    },
    TAKE_ORDER{
        override fun enter(p0: RestaurantCharacter?) {
            //TODO("Not yet implemented")
        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as WaiterEmployeeRestaurantRole
            val seat = role.assignedSeat!!

            val customer = seat.customer?: return
            val customerRole = customer.role as CustomerRestaurantRole
            if (customerRole.orderPlaced) return

            // take the order from customer
            val order = customerRole.orderToPlace?: return
            customerRole.orderPlaced = true
            role.order = order
            entity.stateMachine.changeState(SEND_ORDER_TO_KITCHEN)
            return

        }

        override fun toString(): String {
            return "Waiter: Taking Order"
        }
    },
    SEND_ORDER_TO_KITCHEN{
        override fun enter(entity: RestaurantCharacter) {
            val restaurant = entity.restaurant
            // move to the counter and place the order
            val counter = restaurant.findNearestCounter(entity.position)?: return
            val role = entity.role as WaiterEmployeeRestaurantRole
            role.targetCounter = counter
            entity.targetPosition.set(counter.position.x, counter.position.y-1)

        }

        override fun update(entity: RestaurantCharacter) {
            val role = entity.role as WaiterEmployeeRestaurantRole
            val counter =  role.targetCounter!!
            if (entity.position.x == counter.position.x && entity.position.y == counter.position.y-1) {
                val order = role.order!!
                counter.addOrder(order)
                entity.stateMachine.changeState(IDLE)
                return
            }
        }

        override fun toString(): String {
            return "Waiter: Order to Kitchen"
        }
    },
    SERVE_ORDER{
        override fun enter(entity: RestaurantCharacter) {
            val role = entity.role as WaiterEmployeeRestaurantRole
            val order = role.order!!

            val loc = order.seatLoc
            entity.targetPosition.set(loc.x, loc.y+1)

        }

        override fun update(entity: RestaurantCharacter) {
            if (entity.hasTargetPosition()) return

            val restaurant = entity.restaurant

            val role = entity.role as WaiterEmployeeRestaurantRole
            val order = role.order!!

            // make sure waiter is near the customer
            if (!Utils.isAdjacent(order.customer.position, entity.position)){
                throw Error("Waiter should be near a customer: waiter at ${entity.position} and customer at ${order.customer.position}")
            }
            // place order on table and let customer know
            val table = restaurant.findNearest<RestaurantTable>(order.customer.position)
            table.mealOrdersServed.add(order)
            MessageManager.getInstance().dispatchMessage(entity, order.customer, Messages.MEAL_ARRIVED)

            entity.stateMachine.changeState(IDLE)


        }

        override fun toString(): String {
            return "Waiter: Serving"
        }
    }
    ;
    override fun exit(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
//        TODO("Not yet implemented")
        return false
    }
}
