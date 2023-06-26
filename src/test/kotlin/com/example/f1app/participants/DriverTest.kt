package com.example.f1app.participants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DriverTest {

    lateinit var driver: Driver

    @BeforeEach
    fun setUp() {
        driver = Driver("Russell")
    }

    @Test
    fun `When adding a number of points, then drivers points should increase by that amount`(){
        driver.addPoints(55)
        assertEquals(55, driver.points)

        driver.addPoints(10)
        assertEquals(65, driver.points)
    }

}