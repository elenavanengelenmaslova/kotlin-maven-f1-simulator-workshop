package com.example.f1app.participants

/**
 * Represents a race car in the race.
 *
 * @property carNumber The unique number identifying the car.
 * @property manufacturer The manufacturer of the car.
 * @property currentLap The current lap that the car is on.
 * @property isPitStopNeeded Indicates if the car needs a pit stop.
 * @param numLaps The total number of laps in the race.
 */
class RaceCar(
    val carNumber: Int,
    val manufacturer: String? = null,
    internal var currentLap: Int = 0,
    internal var isPitStopNeeded: Boolean = false,
    numLaps: Int,
) {
    /**
     * Array storing the time taken for each lap.
     * Each index corresponds to the lap number (0-indexed).
     */
    var lapTimes = arrayOfNulls<Double>(numLaps)
        private set

    /**
     * Records the time taken for a particular lap.
     *
     * @param lapNumber The number of the lap (1-indexed).
     * @param time The time taken for the lap.
     */
    fun addLapTime(lapNumber: Int, time: Double) {
        lapTimes[lapNumber - 1] = time
    }

    /**
     * Overrides the standard `toString` method.
     * @return A string representation of the RaceCar instance.
     */
    override fun toString(): String {
        return "RaceCar(carNumber=$carNumber, manufacturer=$manufacturer)"
    }
}
