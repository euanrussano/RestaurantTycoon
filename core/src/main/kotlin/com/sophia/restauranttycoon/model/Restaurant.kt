package com.sophia.restauranttycoon.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.sophia.restauranttycoon.model.furniture.Counter
import com.sophia.restauranttycoon.model.furniture.Furniture
import com.sophia.restauranttycoon.model.furniture.Seat
import com.sophia.restauranttycoon.model.furniture.Stove
import com.sophia.restauranttycoon.model.role.*
import com.sophia.restauranttycoon.model.state.customer.CustomerEatingState
import com.sophia.restauranttycoon.model.state.customer.JustSpawnedState
import com.sophia.restauranttycoon.model.state.customer.PlacingOrderState
import com.sophia.restauranttycoon.model.state.customer.WaitingInQueueState
import com.sophia.restauranttycoon.model.system.CustomerSpawnSystem
import com.sophia.restauranttycoon.model.system.MovementSystem
import com.sophia.restauranttycoon.model.system.RestaurantSystem
import kotlin.reflect.KClass

class Restaurant(
    val tilemap: Array<Array<Tile>>,
    val owned: Array<Array<Boolean>>,

) {
    var day = 0
    var time = 0f

    val entitiesToRelease = mutableListOf<RestaurantCharacter>()
    val customerPool = object : Pool<RestaurantCharacter>(){
        override fun newObject(): RestaurantCharacter {
            val customer = RestaurantCharacter(CustomerRestaurantRole(), 0, 0, this@Restaurant)
            return customer
        }

    }

    // money
    var balance: Int = 0

    // the menu
    val meals = mutableListOf<Meal>(
        Meal("Burger", 100, 5, 5),
        Meal("Pizza", 100, 5, 5),
        Meal("Fries", 100, 5, 5),
        Meal("Salad", 100, 5, 5),

    )


    val endOfQueue: Pair<Int, Int>
        get() {

            var door = -1 to -1
            for (y in tilemap.indices){
                for (x in tilemap[y].indices){
                    val tile = tilemap[y][x]
                    if (tile == Tile.DOOR){
                        door = x to y
                    }
                }
            }
            val queue = customers.filter {
                it.stateMachine.currentState is WaitingInQueueState
            }
            return door.first - queue.size to door.second-1
        }
    val restaurantSystems: List<RestaurantSystem> = listOf(
        CustomerSpawnSystem(1),
        MovementSystem()
    )

    val furnitures = mutableListOf<Furniture>()

    var restaurantCharacters = mutableListOf<RestaurantCharacter>()

    val customers get() = restaurantCharacters.filter { it.role is CustomerRestaurantRole }
    val employees get() = restaurantCharacters.filter { it.role is EmployeeRestaurantRole }

    val customersInQueueAndIncoming get() = customers.filter {
        it.stateMachine.currentState is WaitingInQueueState ||
            it.stateMachine.currentState is JustSpawnedState
    }
    val customersWaitingToOrder get() = customers.filter {
        val state = it.stateMachine.currentState as? PlacingOrderState ?: return@filter false
        return@filter state.orderPlaced == false
    }
    val customersWaitingToEat get() = customers.filter {
        val state = it.stateMachine.currentState as? PlacingOrderState ?: return@filter false
        return@filter state.orderPlaced == true
    }
    val customersEating get() = customers.filter {
        it.stateMachine.currentState is CustomerEatingState
    }




    fun update(delta: Float){
        // update all characters
        for (restaurantCharacter in restaurantCharacters) {
            restaurantCharacter.update(delta, this)
        }
        // update all systems
        for (restaurantSystem in restaurantSystems) {
            restaurantSystem.update(delta, this)
        }

        for (restaurantCharacter in entitiesToRelease) {
            customerPool.free(restaurantCharacter)
            restaurantCharacters -= restaurantCharacter
        }
        entitiesToRelease.clear()

        // advance time
        val delta = GdxAI.getTimepiece().deltaTime
        time += delta
        if (time > 24f){
            day += 1
            time = 0f
            performDailyTasks()
        }
    }

    private fun performDailyTasks() {
        // salary payment
        paySalaries()

        // Maintenance costs - FUTURE
//        performMaintenance()

        // Check for special events or promotions starting tomorrow - FUTURE
//        scheduleEventsForTomorrow()

        // Reset daily specials or menu items - FUTURE
//        resetDailySpecials()

        // Log end of day
        Gdx.app.log("Restaurant", "End of day $day tasks performed.")
    }

    private fun paySalaries() {
        val totalSalaries = employees.sumOf { (it.role as EmployeeRestaurantRole).salary }
        if (balance >= totalSalaries) {
            balance -= totalSalaries
            Gdx.app.log("Restaurant", "Paid total salaries: $totalSalaries")
        } else {
            Gdx.app.log("Restaurant", "Not enough balance to pay salaries!")
            // Handle the scenario where there is not enough money to pay salaries
            // FUTURE:
            // affect morale/productivity?
            // strikes/quitting?
            // loan?
        }
    }

    private fun performMaintenance() {
        // FUTURE
//        val maintenanceCost = furnitures.sumOf { it.maintenanceCost }
//        balance -= maintenanceCost
//        Gdx.app.log("Restaurant", "Maintenance cost $maintenanceCost deducted")
    }

    private fun resetDailySpecials() {
        // FUTURE
//        meals.forEach { it.resetSpecial() }
    }

    private fun scheduleEventsForTomorrow() {
        // FUTURE
        // Example of setting a special event
//        if (Random.nextFloat() < 0.1) {  // 10% chance of a special event
//            val event = Event("Live Music", increaseInCustomers = 20)
//            upcomingEvents.add(event)
//            Gdx.app.log("Restaurant", "Special event planned: ${event.name}")
//        }
    }


    fun addFurniture(furniture: Furniture) {
        furnitures += furniture
    }

    fun buyFurniture(furniture: Furniture): Boolean {
        if (balance < furniture.price) return false
        balance -= furniture.price
        addFurniture(furniture)
        return true
    }

    fun hireWaiter() {
        val role = WaiterEmployeeRestaurantRole()
        hireEmployee(role)
    }
    fun hireCook() {
        val role = CookEmployeeRestaurantRole()
        hireEmployee(role)
    }

    private fun hireEmployee(employeeRestaurantRole: EmployeeRestaurantRole) {
        // spawn new employees in the middle of the restaurant
        val y = tilemap[0].size / 2
        val x = tilemap.size / 2


        val character = RestaurantCharacter(employeeRestaurantRole, x, y, this)
        restaurantCharacters += character
    }

    fun addCustomer() {
        // spawn a customer in the bottom left corner
        val customer = customerPool.obtain()
        restaurantCharacters += customer
        Gdx.app.log("Restaurant", "spawn a customer")

    }

    fun findFreeSeatIfAny(): Seat? {
        return furnitures.firstOrNull { it is Seat && it.customer == null } as? Seat
    }

    fun getAssignedSeatForCustomer(entity: RestaurantCharacter): Seat {
        return furnitures.firstOrNull {
            val seat = it as? Seat ?: return@firstOrNull false
            seat.customer == entity
        } as? Seat ?: throw Exception("seat not found")
    }

    fun findFirstCustomerWaitingToOrder(): RestaurantCharacter? {
        val customer = customers.firstOrNull {
            val state = it.stateMachine.currentState as? PlacingOrderState ?: return@firstOrNull false
            return@firstOrNull state.orderPlaced == false
        }
        return customer
    }

    fun findNearestCounter(sourcePosition: Vector2): Counter {
        val countersSorted =furnitures.filterIsInstance<Counter>().sortedBy {
            val counter = it as Counter
            val dst = counter.position.dst(sourcePosition)
            dst
        }
        return countersSorted.first()

    }

    inline fun <reified T: Furniture> findNearest(sourcePosition: Vector2): T {
        return furnitures
            .filterIsInstance<T>() // Filter instances of T
            .minByOrNull { it.position.dst2(sourcePosition) }
            ?: throw Exception("No ${T::class.simpleName} found")
    }

    fun findNearestFreeStove(position: Vector2): Stove? {
        return furnitures
            .filterIsInstance<Stove>() // Filter instances of T
            .filter{ it.cook == null }
            .minByOrNull { it.position.dst2(position) }
    }

    fun processPaymentForMeal(entity: RestaurantCharacter, meal: Meal) {
        val price = meal.price
        this.balance += price
    }

    fun releaseCustomer(entity: RestaurantCharacter) {
        // free any seats assigned to the customer
        val seat = getAssignedSeatForCustomer(entity)
        seat.customer = null
        entitiesToRelease += entity
    }


}
