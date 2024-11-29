package com.sophia.restauranttycoon.model.system

import com.sophia.restauranttycoon.model.Restaurant
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

class MovementSystem: RestaurantSystem {

    var speed = 1f
    override fun update(delta: Float, restaurant: Restaurant) {
        for (restaurantCharacter in restaurant.restaurantCharacters) {
            val position = restaurantCharacter.position
            val target = restaurantCharacter.targetPosition
            if (target.x == -1f && target.y == -1f) continue

            val dst = target.dst(position.x, position.y)

            val dx = (target.x - position.x).sign
            val dy = (target.y - position.y).sign
            val moveDistance = min(dst, speed * delta)

            position.x += dx * moveDistance
            position.y += dy * moveDistance

            if (abs(position.x - target.x) < 0.01f && abs(position.y - target.y) < 0.01f){
                //Gdx.app.log("MovementSystem", "target reached")
                position.x = target.x
                position.y = target.y
                target.set(-1f, -1f)

            }
        }




    }
}
