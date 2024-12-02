package com.sophia.restauranttycoon.model.system

import com.sophia.restauranttycoon.model.Restaurant

interface RestaurantSystem {

    fun update(delta: Float, restaurant: Restaurant)
    fun onReputationChanged(reputation: Int)
}
