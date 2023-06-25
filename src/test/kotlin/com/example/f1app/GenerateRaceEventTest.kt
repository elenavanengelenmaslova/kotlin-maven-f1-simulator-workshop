package com.example.f1app

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GenerateRaceEventTest {

    //mock randomness provider with Mockk
    private val mockRandomnessProvider: RandomnessProvider = mockk()

    @Test
    fun `When random event is in first 5% then event value is BREAKDOWN`() {
        //any value  of 0 - 4, we are using 0
        every { mockRandomnessProvider.nextInt(100) } returns 0
        assertEquals(RaceEvent.BREAKDOWN, generateRaceEvent(randomnessProvider = mockRandomnessProvider))
    }

    @Test
    fun `When random event is in the next 2% then event value is COLLISION`() {
        // any value of 5 or 6, we are using 6
        every { mockRandomnessProvider.nextInt(100) } returns 6
        assertEquals(RaceEvent.COLLISION, generateRaceEvent(randomnessProvider = mockRandomnessProvider))
    }

    @Test
    fun `When random event is in the other 93% then event value is NORMAL`() {
        // any value of 7 - 99, we are using 99
        every { mockRandomnessProvider.nextInt(100) } returns 99
        assertEquals(RaceEvent.NORMAL, generateRaceEvent(randomnessProvider = mockRandomnessProvider))
    }

}