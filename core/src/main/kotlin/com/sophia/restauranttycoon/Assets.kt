package com.sophia.restauranttycoon

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.sophia.restauranttycoon.model.*
import com.sophia.restauranttycoon.model.furniture.*
import com.sophia.restauranttycoon.model.role.CookEmployeeRestaurantRole
import com.sophia.restauranttycoon.model.role.CustomerRestaurantRole
import com.sophia.restauranttycoon.model.role.RestaurantRole
import com.sophia.restauranttycoon.model.role.WaiterEmployeeRestaurantRole
import ktx.assets.load
import kotlin.reflect.KClass

object Assets {
    private val assetManager = AssetManager()
    private lateinit var tilesetTexSplit: Array<Array<TextureRegion>>

    private val tileTextureMap = mutableMapOf<Tile, TextureRegion>()
    private val roleTextureMap = mutableMapOf<KClass<out RestaurantRole>, TextureRegion>()
    private val furnitureTextureMap = mutableMapOf<KClass<out Furniture>, TextureRegion>()

    fun loadAssets() {
        assetManager.load<Texture>("tileset.png")
        assetManager.finishLoading()

        val tilesetTex = assetManager.get<Texture>("tileset.png").apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

        tilesetTexSplit = TextureRegion(tilesetTex).split(16,16)

        tileTextureMap.putAll(
            mapOf(
                Tile.FLOOR to tilesetTexSplit[0][0],
                Tile.WALL to tilesetTexSplit[17][10],
                Tile.DOOR to tilesetTexSplit[9][2]
            )
        )
        roleTextureMap.putAll(
            mapOf(
                CookEmployeeRestaurantRole::class to tilesetTexSplit[1][30],
                WaiterEmployeeRestaurantRole::class to tilesetTexSplit[1][28],
                CustomerRestaurantRole::class to tilesetTexSplit[0][25]
            )
        )
        furnitureTextureMap.putAll(
            mapOf(
                RestaurantTable::class to tilesetTexSplit[8][2],
                Seat::class to tilesetTexSplit[8][1],
                Sink::class to tilesetTexSplit[7][11],
                Stove::class to tilesetTexSplit[8][11],
                Counter::class to tilesetTexSplit[7][6]
            )
        )

    }

    fun getTileRegion(tile: Tile): TextureRegion {
        return tileTextureMap[tile]!!
    }

    fun getRoleRegion(role: KClass<out RestaurantRole>): TextureRegion {
        return roleTextureMap[role]!!
    }

    fun getFurnitureRegion(furniture: KClass<out Furniture>): TextureRegion {
        return furnitureTextureMap[furniture]!!
    }
}
