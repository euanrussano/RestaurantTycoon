package com.sophia.restauranttycoon.model.furniture

import com.badlogic.gdx.math.Vector2

abstract class Furniture(
    x: Int,
    y: Int,
    val price: Int
) {
    val position: Vector2 = Vector2(x.toFloat(), y.toFloat())
}
