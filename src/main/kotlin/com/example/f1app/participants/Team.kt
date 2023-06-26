package com.example.f1app.participants

import kotlin.random.Random

/**
 * Represents a racing team.
 *
 * @property name The name of the team.
 * @property drivers The list of drivers in the team.
 * @property raceCars The set of race cars in the team.
 * @property mainSponsor The main sponsor of the team. This could be `null` if there is no sponsor.
 */
class Team(
    val name: String,
    val drivers: List<Driver>,
    val raceCars: Set<RaceCar>,
    val mainSponsor: Sponsor? = getRandomSponsor(),
) {
    /**
     * Represents the mapping of drivers to their respective cars in the team.
     */
    val driverCarMap: Map<Driver, RaceCar> = drivers.zip(raceCars).toMap()

    override fun toString(): String {
        return "Team(name='$name', driverCarMap=$driverCarMap)"
    }
}

/**
 * Generates a random sponsor.
 *
 * @return A random [Sponsor] object or `null` if the generated random number is 0.
 */
fun getRandomSponsor(): Sponsor? {
    // Generate a random number from 0 to 10
    val randomNumber = Random.nextInt(0, 11)

    // Return null if the random number is 0, otherwise return a Sponsor
    return if (randomNumber == 0) {
        null
    } else {
        Sponsor(
            name = "Sponsor$randomNumber",
            amount = Random.nextDouble(1_000_000.0, 10_000_000.0)
        )
    }
}
