package com.sophia.restauranttycoon

import com.badlogic.gdx.math.Vector2
import kotlin.math.abs

object Utils {

    fun isAdjacent(pos1: Vector2, pos2: Vector2): Boolean {
        return abs(pos1.x - pos2.x) <= 1 && abs(pos1.y - pos2.y) <= 1
    }

}
