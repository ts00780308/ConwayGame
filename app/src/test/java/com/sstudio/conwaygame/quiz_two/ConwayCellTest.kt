package com.sstudio.conwaygame.quiz_two

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConwayCellTest {
    @Test
    fun testConwayCell() {
        val cell = ConwayCell(1, 2, true)
        assertEquals(1, cell.xIndex)
        assertEquals(2, cell.yIndex)
        assertEquals(true, cell.isAlive)
    }
}