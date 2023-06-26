package com.example.f1app

/**
 * Base class for race-related exceptions in the application.
 *
 * @property message The detailed message of the exception.
 */
open class RaceException(message: String) : Exception(message)

/**
 * Exception indicating that a safety car has been deployed. This usually
 * happens when there is a collision during the race.
 *
 * @property message The detailed message of the exception.
 */
class SafetyCarException(message: String) : RaceException(message)

/**
 * Exception indicating that a yellow flag has been raised. This usually
 * happens when a car breaks down and needs to make a pit stop.
 *
 * @property message The detailed message of the exception.
 */
class YellowFlagException(message: String) : RaceException(message)
