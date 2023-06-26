package com.example.f1app.participants

class Team(
    val name: String,
    val drivers: List<Driver>,
    val raceCars: Set<RaceCar>,
    val mainSponsor: Sponsor? = null,
)  {
    val driverCarMap: Map<Driver, RaceCar> = drivers.zip(raceCars).toMap()

    override fun toString(): String {
        return "Team(name='$name', driverCarMap=$driverCarMap)"
    }

}