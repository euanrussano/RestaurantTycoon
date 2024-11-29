package com.sophia.restauranttycoon.model.system

import com.sophia.restauranttycoon.model.Restaurant

class CustomerSpawnSystem(
    val maxCustomersInQueue: Int
): RestaurantSystem {

    var spawnRate = 5f
    var currentTimer = 0f

    override fun update(delta: Float, restaurant: Restaurant) {
        currentTimer += delta
        if (currentTimer < spawnRate) return
        currentTimer = 0f


        if (restaurant.customersInQueueAndIncoming.size >= maxCustomersInQueue) return

        restaurant.addCustomer()

    }

}
