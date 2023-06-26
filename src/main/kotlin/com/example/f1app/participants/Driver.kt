package com.example.f1app.participants

import java.util.*

/**
 * Represents a driver participant in the race.
 *
 * @property name The name of the driver.
 * @property points The number of points the driver has accumulated. Defaults to 0.
 * @property uuid A unique identifier for the driver.
 */
class Driver(
    val name: String,
    var points: Int = 0,
    val uuid: UUID = UUID.randomUUID(),
) {
    /**
     * Adds points to the driver's current points.
     *
     * @param newPoints The points to be added.
     */
    fun addPoints(newPoints: Int) {
        points += newPoints
    }

    /**
     * Overrides the standard `toString` method.
     * @return A string representation of the Driver instance.
     */
    override fun toString(): String {
        return "Driver(name='$name', points=$points)"
    }

    /**
     * Overrides the standard `equals` method.
     *
     * @param other The other object to compare this instance with.
     * @return true if this instance and other are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Driver

        if (uuid != other.uuid) return false

        return true
    }

    /**
     * Overrides the standard `hashCode` method.
     *
     * @return A hash code value for this instance.
     */
    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
