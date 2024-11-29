package com.sophia.restauranttycoon

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sophia.restauranttycoon.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.toInternalFile
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class RestaurantTycoon : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
        Assets.loadAssets()

        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }
}
