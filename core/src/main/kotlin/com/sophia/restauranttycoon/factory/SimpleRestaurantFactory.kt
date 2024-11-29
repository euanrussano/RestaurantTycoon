package com.sophia.restauranttycoon.factory

import com.sophia.restauranttycoon.model.*
import com.sophia.restauranttycoon.model.furniture.*


class SimpleRestaurantFactory {
    fun create(width: Int, height: Int): Restaurant {
        val tiles = Array(height){
            Array(width){
                Tile.FLOOR
            }
        }
        // surround with walls
        for (i in 0 until height){
            tiles[1][i] = Tile.WALL
            tiles[height-1][i] = Tile.WALL
        }
        for (i in 1 until width){
            tiles[i][0] = Tile.WALL
            tiles[i][width-1] = Tile.WALL
        }
        // door in the middle
        tiles[1][width/2] = Tile.DOOR


        // which tiles are owned (player can build/ place stuff
        val owned = Array(height){
            Array(width){
                true
            }
        }
        // front passwalk is not owned
        for (i in 0 until width){
            owned[0][i] = false
        }

        val restaurant = Restaurant(tiles, owned)

        // add basic furniture
        restaurant.addFurniture(RestaurantTable(2, 2))
        restaurant.addFurniture(Seat(3, 2))
        restaurant.addFurniture(RestaurantTable(2, 4))
        restaurant.addFurniture(Seat(3, 4))
        restaurant.addFurniture(Counter(width -4, height-4))
        restaurant.addFurniture(Stove(width -2, height-2))
        restaurant.addFurniture(Sink(width -3, height-2) )

        // add starter employees
        restaurant.hireWaiter()
        restaurant.hireCook()

        return restaurant
    }

}
