package com.example.f1app

import com.example.f1app.participants.Driver
import com.example.f1app.participants.RaceCar
import com.example.f1app.participants.Team
import kotlin.random.Random

/**
 * Represents a Race in an F1 championship.
 *
 * @property numberOfLaps The total number of laps in the race.
 * @property teams The teams participating in the race.
 * @property currentLap The current lap of the race.
 */
class Race(
    val numberOfLaps: Int,
    val teams: List<Team>,
    var currentLap: Int = 0,
) {
    /**
     * A mutable list of results from the race.
     */
    val raceResults: MutableList<Result> = mutableListOf()

    /**
     * Runs the race by starting the race and ending it with race results.
     */
    fun runRace() {
        start()
        end()
    }

    /**
     * Starts the race and runs each lap.
     */
    fun start() {
        for (lap in 1..numberOfLaps) {
            currentLap = lap
            println("Starting lap $currentLap")
            runLap()
        }
    }

    /**
     * Ends the race, awards points, and displays leaderboard.
     */
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

    /**
     * Represents the result of a Team in the race.
     *
     * @property team The Team this result is for.
     * @property totalTime The total time this team took to complete the race.
     */
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

    /**
     * Represents the result of a race.
     *
     * @property team The Team that this result is for.
     * @property driver The Driver that this result is for.
     * @property car The RaceCar that this result is for.
     * @property totalLapTime The total time that this driver took to complete the race.
     * @property fastestLap The fastest lap time of this driver.
     */
    data class Result(
        val team: Team,
        val driver: Driver,
        val car: RaceCar,
        var totalLapTime: Double = 0.0,
        var fastestLap: Double = Double.MAX_VALUE,
    )

    private fun runLap() {
        teams.forEach { team ->
            team.driverCarMap.forEach { (driver, car) ->
                val result = findOrAddResult(team, driver, car)
                // If the car needs a pit stop, we skip this lap for the driver
                if (car.isPitStopNeeded) {
                    handlePitStop(result)
                } else {
                    runLapForDriver(result)
                }
            }
        }
    }

    private fun findOrAddResult(team: Team, driver: Driver, car: RaceCar) =
        raceResults.find { it.driver == driver } ?: Result(team, driver, car).also { raceResults.add(it) }

    /**
     * Simulates a lap for the specified driver and car.
     *
     * The behaviour of the lap is determined by the [raceEvent] parameter. A [raceEvent] can result
     * in a normal lap, a breakdown, or a collision. In the case of a breakdown or collision,
     * the car's pit stop flag is set and an exception is thrown to signal the event.
     *
     * If the lap is normal, the current lap count for the car is incremented, a random lap time
     * between 1.0 and 2.0 is generated and added to the car's lap times, and the generated lap
     * time is returned.
     *
     * @param driver The Driver for whom the lap is being simulated.
     * @param car The Car that the Driver is driving.
     * @param raceEvent The RaceEvent that will determine the behaviour of the lap.
     * The default is a randomly generated RaceEvent.
     *
     * @throws YellowFlagException If the [raceEvent] is [RaceEvent.BREAKDOWN], a
     * YellowFlagException is thrown indicating that the car broke down.
     * @throws SafetyCarException If the [raceEvent] is [RaceEvent.COLLISION], a
     * SafetyCarException is thrown indicating that the car collided with something.
     *
     * @return The lap time for the driver if the [raceEvent] is [RaceEvent.NORMAL], or throws an
     * exception if the [raceEvent] is either [RaceEvent.BREAKDOWN] or [RaceEvent.COLLISION].
     */
    internal fun simulateLap(driver: Driver, car: RaceCar, raceEvent: RaceEvent = generateRaceEvent()): Double =
        when (raceEvent) {
            RaceEvent.BREAKDOWN -> {
                car.isPitStopNeeded = true
                throw YellowFlagException(
                    "Car ${car.carNumber} broke down - pit stop!"
                )
            }

            RaceEvent.COLLISION -> {
                car.isPitStopNeeded = true
                throw SafetyCarException(
                    "Car #${car.carNumber} collided - pit stop!"
                )
            }

            RaceEvent.NORMAL -> {
                car.currentLap++
                val lapTime = Random.nextDouble(1.0, 2.0)
                car.addLapTime(car.currentLap, lapTime)
                println("Driver ${driver.name} completed lap: $lapTime min")
                lapTime
            }
        }

    private fun displayLeaderboard() {
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

    private fun handlePitStop(result: Result) {
        println("\"Car ${result.car.carNumber} skips this lap.")
        // reset the flag
        result.car.isPitStopNeeded = false
        // add pit stop time
        result.totalLapTime += PITSTOP_TIME
    }

    private fun slowDownLapTimes() {
        // Increase lap times for all drivers to simulate a race slowdown
        raceResults.forEach { it.totalLapTime += SLOWDOWN_TIME }
    }

    private fun runLapForDriver(result: Result) {
        try {
            val lapTime = simulateLap(result.driver, result.car)
            result.totalLapTime += lapTime
            if (lapTime < result.fastestLap) {
                result.fastestLap = lapTime
            }
        } catch (e: SafetyCarException) {
            println("${e.message} Safety car deployed.")
            slowDownLapTimes()
        } catch (e: YellowFlagException) {
            println("${e.message} Yellow flag raised.")
            slowDownLapTimes()
        }
    }

    companion object {
        /**
         * Time taken for a pit stop. Represented in minutes.
         */
        const val PITSTOP_TIME = 5.0

        /**
         * Time added to all drivers' lap times when a slowdown occurs. Represented in minutes.
         */
        const val SLOWDOWN_TIME = 1.0
    }
}

/**
 * Enumeration representing possible events during a race.
 */
enum class RaceEvent {
    /**
     * Represents a normal event, where nothing exceptional happens.
     */
    NORMAL,

    /**
     * Represents a breakdown event, where a car breaks down and requires a pit stop.
     */
    BREAKDOWN,

    /**
     * Represents a collision event, where a collision occurs that leads to a pit stop.
     */
    COLLISION,
}

/**
 * Function to generate a RaceEvent. A breakdown event is generated with `breakdownPercent` chance,
 * a collision event with `collisionPercent` chance, and a normal event otherwise.
 *
 * @param breakdownPercent The chance (as a percentage out of 100) of generating a breakdown event. Defaults to 5.
 * @param collisionPercent The chance (as a percentage out of 100) of generating a collision event. Defaults to 2.
 * @param randomnessProvider Provider of random numbers used for event generation. Defaults to a new instance of `RandomnessProvider`.
 * @return The generated `RaceEvent`.
 */
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

/**
 * Class providing random numbers.
 */
class RandomnessProvider {
    fun nextInt(until: Int): Int {
        return Random.nextInt(until)
    }
}

/**
 * Extension function on `Race.TeamResult` to format it as a leaderboard entry string.
 *
 * @param index The index of this result in the leaderboard.
 * @return A string representing the leaderboard entry for this `TeamResult`.
 */
internal fun Race.TeamResult.format(index: Int): String {
    val teamPosition = "${index + 1}. Team ${team.name}"
    val teamTime = "with total time $totalTime minutes"
    val sponsor = team.mainSponsor?.let { "Sponsored by ${it.name}" }
        ?: "No main sponsor"
    return "$teamPosition $teamTime. $sponsor"
}