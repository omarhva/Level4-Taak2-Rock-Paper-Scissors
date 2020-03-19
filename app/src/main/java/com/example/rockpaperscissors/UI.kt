package com.example.rockpaperscissors

/**
 * @author Omar Mulla Ibrahim
 * Student Nr 500766035
 */

/**
 * get result in UI
 */

class UI {
    companion object {
        fun getDrawableFromChoice(result: String): Int {
            if (result == Options.SCISSOR.toString()) {
                return R.drawable.scissors
            }
            if (result == Options.PAPER.toString()) {
                return R.drawable.paper
            }
            return R.drawable.rock
        }
    }
}