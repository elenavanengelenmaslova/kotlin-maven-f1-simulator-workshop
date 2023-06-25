package com.example.f1app.participants

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RaceCarTest {

    lateinit var car: RaceCar

    @BeforeEach
    fun setUp() {
        car = RaceCar(carNumber = 1, numLaps = 5)
    }

    @Test
    fun `When lap time is added then it should be in correct position in the lapTimes array`() {
        car.addLapTime(1, 2.5)
        assertEquals(
            2.5,
            car.lapTimes[0],
            "First element of lapTimes should contain the lap time added with lap number 1"
        )
    }
}