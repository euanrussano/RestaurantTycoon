package com.sophia.restauranttycoon.model.system

import com.sophia.restauranttycoon.model.Restaurant

class CustomerSpawnSystem(
    val maxCustomersInQueue: Int
): RestaurantSystem {

    val reputationThreshold = 50
    val baseSpawnRate = 5f
    var spawnRate = 5f
    var currentTimer = 0f

    override fun update(delta: Float, restaurant: Restaurant) {
        currentTimer += delta
        if (currentTimer < spawnRate) return
        currentTimer = 0f


        if (restaurant.customersInQueueAndIncoming.size >= maxCustomersInQueue) return

        restaurant.addCustomer()

    }

    override fun onReputationChanged(reputation: Int) {
        // Update customer inflow based on new reputation
        spawnRate = baseSpawnRate * (1 + (reputation - reputationThreshold)/10)
    }

}
