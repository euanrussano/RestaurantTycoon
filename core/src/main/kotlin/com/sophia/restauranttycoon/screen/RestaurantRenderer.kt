package com.sophia.restauranttycoon.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.sophia.restauranttycoon.Assets
import com.sophia.restauranttycoon.model.Restaurant

class RestaurantRenderer(val batch: SpriteBatch, val restaurant: Restaurant) {
    fun render() {
        renderMap()
        renderFurnitures()
        renderCharacters()
    }

    private fun renderMap() {
        val tiles = restaurant.tilemap

        for (y in tiles.indices){
            for (x in tiles[y].indices){
                val tile = tiles[y][x]
                val region = Assets.getTileRegion(tile)
                batch.draw(region, x.toFloat(), y.toFloat(), 1f, 1f)
            }
        }
    }

    private fun renderFurnitures() {
        val furnitures = restaurant.furnitures

        for (furniture in furnitures){
            val position = furniture.position
            val region = Assets.getFurnitureRegion(furniture::class)
            batch.draw(region, position.x, position.y, 1f, 1f)

        }
    }

    private fun renderCharacters() {
        val characters = restaurant.restaurantCharacters

        for (character in characters){
            val region = Assets.getRoleRegion(character.role::class)
            val position = character.position
            batch.draw(region, position.x, position.y, 1f, 1f)
        }
    }





}