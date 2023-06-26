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
    val raceResults: MutableList<Result> = mutableListOf()

    fun runRace() {
        start()
        end()
    }

    /**
     * Starts the race.
     */
    fun start() {
        for (lap in 1..numberOfLaps) {
            currentLap = lap
            println("Starting lap $currentLap")
            runLap()
        }
    }

    fun end() {
        awardPoints()
        displayLeaderboard()
        displayTeamLeaderboard()
    }

    /**
     * Awards points to the top 10 finishers.
     */
    private fun awardPoints() {
        // Points corresponding to the positions 1st through 10th.
        val pointsList = listOf(25, 18, 15, 12, 10, 8, 6, 4, 2, 1)

        // Award points to the top 10 finishers
        for ((index, result) in raceResults.take(10).withIndex()) {
            // The points for this position
            // are at the same index in the pointsList
            val points = pointsList.getOrNull(index) ?: 0
            result.driver.addPoints(points)
        }
    }

    data class TeamResult(
        val team: Team,
        val totalTime: Double,
    )

    private fun displayTeamLeaderboard() {
        println("\n--- TEAM LEADERBOARD ---")
        val teamResults = teams.toSortedTeamResults(raceResults)

        teamResults.forEachIndexed { index, result ->
            println(result.format(index))
        }
    }

    // Extension function on List<Team> to generate TeamResults and sort them by total time
    private fun List<Team>.toSortedTeamResults(raceResults: List<Result>): List<TeamResult> {
        return this.map { team ->
            val teamTime = raceResults.filter { it.team == team }
                .sumOf { it.totalLapTime }
            TeamResult(team, teamTime)
        }.sortedBy { it.totalTime }
    }

    data class Result(
        val team: Team,
        val driver: Driver,
        val car: RaceCar,
        var totalLapTime: Double = 0.0,
        var fastestLap: Double = Double.MAX_VALUE,
    )

    fun runLap() {
        teams.forEach { team ->
            team.driverCarMap.forEach { (driver, car) ->
                val result = findOrAddResult(team, driver, car)

                // If the car needs a pit stop, skip this lap for the driver
                if (car.isPitStopNeeded) {
                    println("Car ${car.carNumber} skips this lap.")
                    car.isPitStopNeeded = false
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

    private fun simulateLap(driver: Driver, car: RaceCar): Double =
        when (generateRaceEvent()) {
            RaceEvent.BREAKDOWN -> {
                car.isPitStopNeeded = true
                println("Car ${car.carNumber} broke down - pit stop!")
                PITSTOP_TIME
            }

            RaceEvent.COLLISION -> {
                car.isPitStopNeeded = true
                println("Car #${car.carNumber} collided - pit stop!")
                PITSTOP_TIME
            }

            RaceEvent.NORMAL -> {
                car.currentLap++
                val lapTime = Random.nextDouble(1.0, 2.0)
                car.addLapTime(car.currentLap, lapTime)
                println("Driver ${driver.name} completed lap: $lapTime min")
                lapTime
            }
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

fun generateRaceEvent(
    breakdownPercent: Int = 5,
    collisionPercent: Int = 2,
    randomnessProvider: RandomnessProvider = RandomnessProvider(),
): RaceEvent {
    val totalExceptionPercent = breakdownPercent + collisionPercent
    val event = randomnessProvider.nextInt(100).let {
        when {
            it < breakdownPercent -> RaceEvent.BREAKDOWN
            it < totalExceptionPercent -> RaceEvent.COLLISION
            else -> RaceEvent.NORMAL
        }
    }
    return event
}

class RandomnessProvider {
    fun nextInt(until: Int): Int {
        return Random.nextInt(until)
    }
}

/**
 *  Extension function on TeamResult
 *  to print the result in the desired format
 */
internal fun Race.TeamResult.format(index: Int): String {
    val teamPosition = "${index + 1}. Team ${team.name}"
    val teamTime = "with total time $totalTime minutes"
    val sponsor = team.mainSponsor?.let { "Sponsored by ${it.name}" }
        ?: "No main sponsor"
    return "$teamPosition $teamTime. $sponsor"
}
