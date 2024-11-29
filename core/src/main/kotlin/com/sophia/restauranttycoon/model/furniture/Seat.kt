package com.sophia.restauranttycoon.model.furniture

import com.sophia.restauranttycoon.model.RestaurantCharacter

class Seat(x: Int, y: Int) : Furniture(x, y, 100) {

    var customer: RestaurantCharacter? = null
}
