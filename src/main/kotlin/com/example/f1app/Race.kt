package com.example.f1app

class Race(
    val numberOfLaps: Int,
    var currentLap: Int = 0,
) {
    companion object {
        const val PITSTOP_TIME = 5.0 // 5 minutes
    }
}