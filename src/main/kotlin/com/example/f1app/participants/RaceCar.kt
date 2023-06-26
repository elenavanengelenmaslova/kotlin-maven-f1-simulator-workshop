package com.example.f1app.participants

class RaceCar(
    val carNumber: Int,
    val manufacturer: String? = null,
    internal var currentLap: Int = 0,
    internal var isPitStopNeeded: Boolean = false,
    numLaps: Int,
) {
    var lapTimes = arrayOfNulls<Double>(numLaps)
        private set

    fun addLapTime(lapNumber: Int, time: Double) {
        lapTimes[lapNumber - 1] = time
    }

    override fun toString(): String {
        return "RaceCar(carNumber=$carNumber, manufacturer=$manufacturer)"
    }


}
