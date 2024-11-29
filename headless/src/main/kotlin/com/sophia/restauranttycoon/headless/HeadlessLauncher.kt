@file:JvmName("HeadlessLauncher")

package com.sophia.restauranttycoon.headless

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.sophia.restauranttycoon.RestaurantTycoon

/** Launches the headless application. Can be converted into a server application or a scripting utility. */
fun main() {
    HeadlessApplication(RestaurantTycoon(), HeadlessApplicationConfiguration().apply {
        // When this value is negative, RestaurantTycoon#render() is never called:
        updatesPerSecond = -1
    })
}
