package com.example.f1app

import com.example.f1app.participants.Driver
import com.example.f1app.participants.RaceCar
import com.example.f1app.participants.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val NUMBER_OF_LAPS = 5

internal class RaceTest {

    private lateinit var race: Race

    @BeforeEach
    fun setUp() {
        val teamRedBull = Team(
            "Red Bull",
            listOf(
                Driver("Verstappen"),
                Driver("Perez")
            ),
            setOf(
                RaceCar(
                    carNumber = 1,
                    numLaps = NUMBER_OF_LAPS,
                ),
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
    }

    @Test
    fun `When 4 drivers then race should have 4 results after race has run`() {
        race.start()
        assertEquals(4, race.raceResults.size)
        assertEquals(5, race.currentLap)
    }
}