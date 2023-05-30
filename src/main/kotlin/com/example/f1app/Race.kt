package com.example.f1app

import com.example.f1app.participants.Team

class Race(
    val numberOfLaps: Int,
    val teams: List<Team>,
    var currentLap: Int = 0,
) {
    companion object {
        const val PITSTOP_TIME = 5.0 // 5 minutes
    }
}