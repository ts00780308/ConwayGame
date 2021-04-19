package com.sstudio.conwaygame.quiz_two

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConwayCellTest {
    @Test
    fun testDie() {
        val cell = ConwayCell(1, 2, true)
        cell.die()
        assertEquals(false, cell.isAlive)
    }

    @Test
    fun testReborn() {
        val cell = ConwayCell(1, 2, false)
        cell.reborn()
        assertEquals(true, cell.isAlive)
    }
}