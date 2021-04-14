package com.sstudio.conwaygame.quiz_two

data class ConwayCell(
    val xIndex: Int,
    val yIndex: Int,
    var isAlive: Boolean
) {
    /**
     * Make cell die by setting [isAlive] to false.
     */
    fun die() {
        isAlive = false
    }

    /**
     * Make cell reborn by setting [isAlive] to true.
     */
    fun reborn() {
        isAlive = true
    }
}