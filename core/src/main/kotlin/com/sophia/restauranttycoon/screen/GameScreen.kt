package com.sophia.restauranttycoon.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.sophia.restauranttycoon.Assets
import com.sophia.restauranttycoon.RestaurantTycoon
import com.sophia.restauranttycoon.factory.SimpleRestaurantFactory
import com.sophia.restauranttycoon.model.RestaurantCharacter
import com.sophia.restauranttycoon.model.furniture.*
import ktx.actors.centerPosition
import ktx.actors.onChange
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

import ktx.graphics.use
import ktx.math.vec2
import ktx.math.vec3
import ktx.scene2d.*
import kotlin.math.roundToInt

class GameScreen(restaurantTycoon: RestaurantTycoon): KtxScreen, KtxInputAdapter {
    private var placingFurniture: Furniture? = null

    private val moneyLabel: Label
    private val dayLabel: Label
    private val timeLabel: Label

    private val customersInQueueLabel: Label
    private val customersWaitingToOrderLabel: Label
    private val customersWaitingToEatLabel: Label
    private val cusTomersEatingLabel: Label

    private val currentToolLabel: Label

    private val viewportSize: Float = 20f
    private val engine = PooledEngine()

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()

    private val restaurantFactory = SimpleRestaurantFactory()
    private val restaurant = restaurantFactory.create(10, 10)

    private val restaurantRenderer = RestaurantRenderer(batch, restaurant)

    private val stage = Stage()

    init {
        stage.actors {
            table {
                setFillParent(true)
                // top bar
                table {
                    background = skin.newDrawable("white", Color.DARK_GRAY)
                    this.defaults().padRight(5f)
                    it.growX()

                    label("$ ")
                    moneyLabel = label("0")
                    add().growX()
                    label("Day ")
                    dayLabel = label("${restaurant.day}")
                    label(", ")
                    timeLabel = label("${restaurant.time.roundToInt()}")
                    label(":00")

                }
                row()
                table {
                    it.grow()
                    table {
                        it.grow()
                    }
                    table {
                        background = skin.newDrawable("white", Color.DARK_GRAY)
                        it.growY()
                        this.top().left()
                        this.defaults().left().pad(5f)
                        label("Customers in queue: ")
                        customersInQueueLabel = label("0")
                        row()
                        label("Customers waiting to order: ")
                        customersWaitingToOrderLabel = label("0")
                        row()
                        label("Customers waiting to eat: ")
                        customersWaitingToEatLabel = label("0")
                        row()
                        label("Customers eating: ")
                        cusTomersEatingLabel = label("0")
                        row()
                        table {
                            this.defaults().pad(5f)
                            this.top().left()
                            button{
                                pad(5f)
                                image(Assets.getFurnitureRegion(RestaurantTable::class))
                                onChange {
                                    placingFurniture = RestaurantTable(0,0)
                                }
                            }
                            button{
                                pad(5f)
                                image(Assets.getFurnitureRegion(Seat::class))
                                onChange {
                                    placingFurniture = Seat(0,0)
                                }
                            }
                            button{
                                pad(5f)
                                image(Assets.getFurnitureRegion(Stove::class))
                                onChange {
                                    placingFurniture = Stove(0,0)
                                }
                            }
                            textButton("X"){
                                onChange {
                                    placingFurniture = null
                                }
                            }
                        }
                        row()
                        table {
                            this.defaults().pad(5f)
                            this.top().left()
                            textButton("Hire waiter"){
                                pad(5f)
                                onChange {
                                    restaurant.hireWaiter()
                                }
                            }
                            row()
                            textButton("Hire cook"){
                                pad(5f)
                                onChange {
                                    restaurant.hireCook()
                                }
                            }
                        }
                    }

                }
                row()
                // bottom bar
                table {
                    background = skin.newDrawable("white", Color.DARK_GRAY)
                    it.growX()
                    label("TOOL: ")
                    currentToolLabel = label("Sel")

                }

            }
        }
    }

    override fun show() {
        super.show()
        // center camera on middle of the map

        val im = InputMultiplexer()
        im.addProcessor(stage)
        im.addProcessor(this)
        Gdx.input.inputProcessor = im
    }

    override fun hide() {
        super.hide()
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        val r = width.toFloat() / height.toFloat()
        camera.setToOrtho(false, r*viewportSize, viewportSize)
        val (height2, width2) = restaurant.tilemap.size to restaurant.tilemap[0].size
        camera.position.set(width2/2f, height2/2f, 0f)
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        super.render(delta)

        GdxAI.getTimepiece().update(delta)

        restaurant.update(delta*3)

        camera.update()
        batch.use(camera){
            restaurantRenderer.render()
        }

        updateUI()
        stage.viewport.apply()
        stage.act()
        stage.draw()
    }

    private fun updateUI() {
        moneyLabel.txt = "${restaurant.balance}"
        dayLabel.txt = "${restaurant.day}"
        timeLabel.txt = "${restaurant.time.roundToInt()}"

        customersInQueueLabel.txt = "${restaurant.customersInQueueAndIncoming.size}"
        customersWaitingToOrderLabel.txt = "${restaurant.customersWaitingToOrder.size}"
        customersWaitingToEatLabel.txt = "${restaurant.customersWaitingToEat.size}"
        cusTomersEatingLabel.txt = "${restaurant.customersEating.size}"

        currentToolLabel.txt = "Sel"
        placingFurniture?.let {
            currentToolLabel.txt = it::class.simpleName.toString()
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val unproj = camera.unproject(vec3(screenX.toFloat(), screenY.toFloat(), 0f))
        val (y, x) = unproj.y.toInt() to unproj.x.toInt()

        // placing furniture?
        if (placingFurniture != null) {
            placingFurniture!!.position.set(x.toFloat(), y.toFloat())
            val bought = restaurant.buyFurniture(placingFurniture!!)
            placingFurniture = null
            if (!bought) {
                val dialog = scene2d.dialog("Not enough money!"){
                    padTop(10f)
                    this.defaults().pad(5f)
                    textButton("OK"){
                        onChange {
                            this@dialog.remove()
                        }
                    }
                    pack()
                }
                stage.addActor(dialog)
                dialog.centerPosition(stage.width, stage.height)
            }
            return true
        }

        // clicked on a character?
        restaurant.restaurantCharacters
            .firstOrNull { it.position.x.toInt() == x && it.position.y.toInt() == y }
            ?.let { character ->
                showCharacterInfo(character)
                return true
            }

        // clicked on a furniture?
        restaurant.furnitures
            .firstOrNull { it.position.x.toInt() == x && it.position.y.toInt() == y }
            ?.let { furniture ->
                showFurnitureInfo(furniture)
                return true
            }
        return false

    }

    private fun showFurnitureInfo(furniture: Furniture) {
        val characterInfoWindow = scene2d.window("Furniture info") {
            padTop(10f)
            this.defaults().pad(5f)
            label(furniture::class.simpleName.toString())
            row()
            if (furniture is RestaurantTable) {
                label("Served orders: ${furniture.mealOrdersServed.size}")
            }
            if (furniture is Seat) {
                label("Customers seated: ${if (furniture.customer != null) "Yes" else "No"}")
            }
            if (furniture is Counter){
                label("Meal orders ready: ${furniture.mealOrdersReady.size}")
                row()
                label("Meal orders to cook: ${furniture.mealOrdersToCook.size}")
            }
            if (furniture is Stove){
                label("Is cooking: ${if (furniture.cook != null) "Yes" else "No"}")
            }
            row()
            textButton("OK"){
                onChange {
                    this@window.remove()
                    println("here")
                }
            }
            pack()
        }

        stage.addActor(characterInfoWindow)
        characterInfoWindow.centerPosition(stage.width, stage.height)
    }

    private fun showCharacterInfo(character: RestaurantCharacter) {
        val characterInfoWindow = scene2d.window("Character info") {
            padTop(10f)
            this.defaults().pad(5f)
            row()
            label("State: ${character.stateMachine.currentState}")
            row()
            character.hungerStateMachine?.let {
                label("Hunger: ${it.currentState}")
            }
            row()
            label("Satisfaction: ${character.satisfaction}")
            row()
            textButton("OK"){
                onChange {
                    this@window.remove()
                    println("here")
                }
            }
            pack()
        }

        stage.addActor(characterInfoWindow)
        characterInfoWindow.centerPosition(stage.width, stage.height)
    }

}
