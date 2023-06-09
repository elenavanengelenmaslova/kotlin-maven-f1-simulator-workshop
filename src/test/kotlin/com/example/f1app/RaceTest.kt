package com.example.f1app

import com.example.f1app.participants.Driver
import com.example.f1app.participants.RaceCar
import com.example.f1app.participants.Team
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val NUMBER_OF_LAPS = 5

internal class RaceTest {

    private lateinit var race: Race
    private lateinit var outContent: ByteArrayOutputStream
    private lateinit var driver1RedBull: Driver
    private lateinit var car1RedBull: RaceCar

    @BeforeEach
    fun `Set up SUT`() {
        driver1RedBull = Driver("Verstappen")
        car1RedBull = RaceCar(
            carNumber = 1,
            numLaps = NUMBER_OF_LAPS,
        )
        val teamRedBull = Team(
            "Red Bull",
            listOf(
                driver1RedBull,
                Driver("Perez")
            ),
            setOf(
                car1RedBull,
                RaceCar(
                    carNumber = 11,
                    numLaps = NUMBER_OF_LAPS,
                )
            )
        )
        val teamMercedes = Team(
            "Mercedes",
            listOf(
                Driver("Hamilton"),
                Driver("Russell")
            ),
            setOf(
                RaceCar(
                    carNumber = 44,
                    numLaps = NUMBER_OF_LAPS,
                ),
                RaceCar(
                    carNumber = 63,
                    numLaps = NUMBER_OF_LAPS,
                )
            )
        )
        race = Race(NUMBER_OF_LAPS, listOf(teamRedBull, teamMercedes))

        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
    }

    @Test
    fun `When 4 drivers then race should have 4 results after race has run`() {
        race.start()
        assertEquals(4, race.raceResults.size, "Race should have 4 results, one for each driver")
        assertEquals(5, race.currentLap)
    }

    @Test
    fun `When 4 drivers and 2 teams then race should race results for 4 drivers and team result for 2 teams`() {
        //run race first
        race.start()
        //produce race results
        race.end()
        assertTrue(outContent.toString().contains("--- LEADERBOARD ---"))
        assertTrue(outContent.toString().contains("Driver Verstappen in car #1"))
        assertTrue(outContent.toString().contains("Driver Russell in car #63"))
        assertTrue(outContent.toString().contains("Driver Hamilton in car #44"))
        assertTrue(outContent.toString().contains("Driver Perez in car #11"))

        assertTrue(outContent.toString().contains("--- TEAM LEADERBOARD ---"))
        assertTrue(outContent.toString().contains("Team Red Bull with total time"))
        assertTrue(outContent.toString().contains("Team Mercedes with total time"))
    }

    @Test
    fun `When BREAKDOWN event then simulateLap shall throw YellowFlagException`() {
        assertFailsWith<YellowFlagException> {
            race.simulateLap(driver = driver1RedBull, car = car1RedBull, RaceEvent.BREAKDOWN)
        }
    }

    @Test
    fun `When COLLISION event then simulateLap shall throw SafetyCarException`() {
        assertFailsWith<SafetyCarException> {
            race.simulateLap(driver = driver1RedBull, car = car1RedBull, RaceEvent.COLLISION)
        }
    }

    @Test
    fun `When NORMAL event then simulateLap shall return lap time`() {
        assertNotNull(race.simulateLap(driver = driver1RedBull, car = car1RedBull, RaceEvent.NORMAL))
    }

    @AfterEach
    fun `Reset the System in and System out`() {
        System.setOut(System.out)
    }

}