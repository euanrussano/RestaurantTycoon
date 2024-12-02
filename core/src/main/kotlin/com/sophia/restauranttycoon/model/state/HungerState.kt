package com.sophia.restauranttycoon.model.state

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.sophia.restauranttycoon.model.RestaurantCharacter

enum class HungerState(private val penalty: Int): State<RestaurantCharacter> {
    WELL_FED(-1){
        override fun getNextState(): State<RestaurantCharacter> = NORMAL
    },
    NORMAL(0){
        override fun getNextState(): State<RestaurantCharacter> = HUNGRY
    },
    HUNGRY(1){
        override fun getNextState(): State<RestaurantCharacter> = STARVING
    },
    STARVING(2){
        override fun getNextState(): State<RestaurantCharacter> = STARVING
    };

    override fun enter(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun update(entity: RestaurantCharacter) {
        val hungerStateMachine = entity.hungerStateMachine?: return
        val currentState = entity.stateMachine.currentState
        if (currentState is CustomerState && currentState == CustomerState.EAT){
            if (hungerStateMachine.currentState != WELL_FED){
                hungerStateMachine.changeState(WELL_FED)
                entity.hungerClock = 0f
                entity.hungerPenaltyClock = 0f
                return
            }
        }
        entity.hungerClock += GdxAI.getTimepiece().deltaTime
        entity.hungerPenaltyClock += GdxAI.getTimepiece().deltaTime
        if (entity.hungerPenaltyClock >= 1f) {
            entity.hungerPenaltyClock -= 1f
            entity.hungerPenalty -=(hungerStateMachine.currentState as HungerState).penalty
        }
        if (entity.hungerClock > entity.hungerClockDuration) {
            entity.hungerClock = 0f
            entity.hungerStateMachine.changeState(getNextState())
        }
    }

    abstract fun getNextState(): State<RestaurantCharacter>?

    override fun exit(entity: RestaurantCharacter) {
//        TODO("Not yet implemented")
    }

    override fun onMessage(entity: RestaurantCharacter, telegram: Telegram): Boolean {
//        TODO("Not yet implemented")
        return false
    }



}
