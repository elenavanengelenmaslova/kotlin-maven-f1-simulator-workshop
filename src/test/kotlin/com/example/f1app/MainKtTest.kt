package com.example.f1app

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

internal class MainKtTest {

    private lateinit var outContent: ByteArrayOutputStream

    @BeforeEach
    fun `Set up System in and System out`(){
        val input = "3\n2\nRed Bull\nVerstappen\nPerez\nMercedes\nHamilton\nRussell\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
    }

    @Test
    fun `When user enters answer to every question to set up a race then construct and run race`() {

        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))

        main()

        assertTrue(outContent.toString().contains("Enter number of laps"))
        assertTrue(outContent.toString().contains("Enter number of teams"))
        assertTrue(outContent.toString().contains("Enter name for team"))
        assertTrue(outContent.toString().contains("--- LEADERBOARD ---"))
    }

    @AfterEach
    fun `Reset the System in and System out`(){
        System.setOut(System.out)
        System.setIn(System.`in`)
    }
}