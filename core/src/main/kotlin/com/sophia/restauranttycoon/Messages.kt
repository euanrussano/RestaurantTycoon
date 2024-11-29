package com.sophia.restauranttycoon

object Messages {
    var value = 0
    fun next() = value++
    val MEAL_ARRIVED: Int = next()
}
