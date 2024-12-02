package com.sophia.restauranttycoon.model

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.StackStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import com.sophia.restauranttycoon.Messages
import com.sophia.restauranttycoon.model.role.CustomerRestaurantRole
import com.sophia.restauranttycoon.model.role.RestaurantRole
import com.sophia.restauranttycoon.model.state.HungerState

class RestaurantCharacter(
    val role: RestaurantRole,
    x: Int,
    y: Int,
    var restaurant: Restaurant
): Telegraph, Poolable {



    var targetPosition: Vector2 = Vector2(-1f, -1f)
    val stateMachine = DefaultStateMachine<RestaurantCharacter, State<RestaurantCharacter>>(this, role.startingState)
    val position = Vector2(x.toFloat(), y.toFloat())

    val hungerStateMachine = if (role.hasHunger) DefaultStateMachine<RestaurantCharacter, State<RestaurantCharacter>>(this, HungerState.NORMAL) else null
    var hungerClock = 0f
    val hungerClockDuration: Float = 5f
    var hungerPenaltyClock = 0f
    var hungerPenalty: Int = 0

    var satisfaction = 70
        private set
    private val baseSatisfaction = 70

    init {
        MessageManager.getInstance().addListener(this, Messages.MEAL_ARRIVED)
        MessageManager.getInstance().addListener(this, Messages.CUSTOMER_LEFT_QUEUE)
    }

    fun update(delta: Float, restaurant: Restaurant) {
        stateMachine.update()
        hungerStateMachine?.update()
        updateSatisfaction()
    }

    private fun updateSatisfaction() {
        satisfaction = baseSatisfaction - hungerPenalty
    }

    fun clearTargetPosition() {
        targetPosition.set(-1f, -1f)
    }

    fun hasTargetPosition(): Boolean {
        return targetPosition.x != -1f && targetPosition.y != -1f
    }

    override fun handleMessage(p0: Telegram?): Boolean {
        return stateMachine.handleMessage(p0) &&
            (hungerStateMachine?.handleMessage(p0) ?: false)

    }

    override fun reset() {
        role.reset()
        stateMachine.changeState(role.startingState)
        hungerStateMachine?.changeState(HungerState.NORMAL)
        position.set(0f, 0f)
        targetPosition.set(-1f, -1f)
    }


}

