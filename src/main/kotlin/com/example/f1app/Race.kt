package com.example.f1app

import com.example.f1app.participants.Driver
import com.example.f1app.participants.RaceCar
import com.example.f1app.participants.Team
import kotlin.random.Random

class Race(
    val numberOfLaps: Int,
    val teams: List<Team>,
    var currentLap: Int = 0,
) {
    private val raceResults: MutableList<Result> = mutableListOf()

    data class Result(
        val team: Team,
        val driver: Driver,
        val car: RaceCar,
        var totalLapTime: Double = 0.0,
        var fastestLap: Double = Double.MAX_VALUE,
    )

    fun runRace() {
        runLap()
        displayLeaderboard()
    }

    fun runLap() {
        teams.forEach { team ->
            team.driverCarMap.forEach { (driver, car) ->
                val result = findOrAddResult(team, driver, car)

                // If the car needs a pit stop, we skip this lap for the driver
                if (car.isPitStopNeeded) {
                    println("Car #${car.carNumber} of driver ${driver.name} is in the pit stop and skips this lap.")
                } else {
                    val lapTime = simulateLap(driver, car)
                    result.totalLapTime += lapTime
                    if (lapTime < result.fastestLap) {
                        result.fastestLap = lapTime
                    }
                }
            }
        }
    }

    fun findOrAddResult(team: Team, driver: Driver, car: RaceCar) =
        raceResults.find { it.driver == driver } ?: Result(team, driver, car).also { raceResults.add(it) }

    fun simulateLap(driver: Driver, car: RaceCar): Double {
        val lapTime = Random.nextDouble(1.0, 2.0)
        car.addLapTime(car.currentLap++, lapTime)
        println("Driver ${driver.name} in car #${car.carNumber} completed lap in $lapTime minutes.")
        return lapTime
    }

    fun displayLeaderboard() {
        println("\n--- LEADERBOARD ---")
        raceResults.sortBy { it.totalLapTime }
        raceResults.forEachIndexed { index, result ->
            val leaderboardEntry = """
            |${index + 1}. Driver ${result.driver.name} in car #${result.car.carNumber}
            |from team ${result.team.name} with total time ${result.totalLapTime} minutes
            |(fastest lap: ${result.fastestLap} minutes)
            """.trimMargin()
            println(leaderboardEntry)
        }
    }

    companion object {
        const val PITSTOP_TIME = 5.0 // 5 minutes
    }
}

enum class RaceEvent {
    NORMAL,
    BREAKDOWN,
    COLLISION,
}