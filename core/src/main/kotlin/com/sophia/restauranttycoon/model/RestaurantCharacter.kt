package com.sophia.restauranttycoon.model

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.model.role.CustomerRestaurantRole
import com.sophia.restauranttycoon.model.role.RestaurantRole

class RestaurantCharacter(
    val role: RestaurantRole,
    x: Int,
    y: Int,
    var restaurant: Restaurant
): Telegraph, Poolable {


    var targetPosition: Vector2 = Vector2(-1f, -1f)
    val startingState = role.startingState
    val stateMachine = DefaultStateMachine<RestaurantCharacter, State<RestaurantCharacter>>(this, startingState)
    val position = Vector2(x.toFloat(), y.toFloat())

    init {
        MessageManager.getInstance().addListener(this, Messages.MEAL_ARRIVED)
    }

    fun update(delta: Float, restaurant: Restaurant) {
        stateMachine.update()
    }

    fun clearTargetPosition() {
        targetPosition.set(-1f, -1f)
    }

    fun hasTargetPosition(): Boolean {
        return targetPosition.x != -1f && targetPosition.y != -1f
    }

    override fun handleMessage(p0: Telegram?): Boolean {
        return stateMachine.handleMessage(p0)
    }

    override fun reset() {
        stateMachine.changeState(startingState)
        position.set(0f, 0f)
        targetPosition.set(-1f, -1f)
    }


}
