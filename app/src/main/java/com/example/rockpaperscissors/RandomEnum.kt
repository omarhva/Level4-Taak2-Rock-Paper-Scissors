package com.example.rockpaperscissors

import kotlin.random.Random
/**
 * @author Omar Mulla Ibrahim
 * Student Nr 500766035
 */

/**
 * here is a Ramdom Enum class to get random integer for the choices of the computer be handeld
 */
class RandomEnum {
    companion object {
        private val SEED = Math.random().toInt()
        private val RANDOM = Random(SEED)
        fun randomEnum(): Options {
            return Options.values()[RANDOM.nextInt(Options.values().size)]
        }
    }

}