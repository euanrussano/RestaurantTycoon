package com.sophia.restauranttycoon

object Messages {

    var value = 0
    fun next() = value++
    val MEAL_ARRIVED: Int = next()
    val CUSTOMER_LEFT_QUEUE: Int = next()
}
