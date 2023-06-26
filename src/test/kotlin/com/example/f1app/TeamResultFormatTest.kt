package com.example.f1app

import com.example.f1app.participants.Driver
import com.example.f1app.participants.RaceCar
import com.example.f1app.participants.Sponsor
import com.example.f1app.participants.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TeamResultFormatTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `When there is a sponsor, format should contain sponsor name`() {
        val teamResult = Race.TeamResult(
            Team(
                name = "Red Bull",
                drivers = listOf(Driver("Verstappen")),
                raceCars = setOf(RaceCar(carNumber = 1, numLaps = 5)),
                Sponsor("Jumbo", 5000.0),
            ),
            5.5,
        )
        assertEquals("1. Team Red Bull with total time 5.5 minutes. Sponsored by Jumbo", teamResult.format(0))

    }

    @Test
    fun `When there is no sponsor, format should contain default no sponsor text`() {
        val teamResult = Race.TeamResult(
            Team(
                name = "Red Bull",
                drivers = listOf(Driver("Verstappen")),
                raceCars = setOf(RaceCar(carNumber = 1, numLaps = 5)),
                null,
            ),
            5.5
        )
        assertEquals("1. Team Red Bull with total time 5.5 minutes. No main sponsor", teamResult.format(0))

    }
}