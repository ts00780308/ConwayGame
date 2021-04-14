package com.sstudio.conwaygame.quiz_two

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConwayWorldTest {

    companion object {
        private const val COLUMN_SIZE = 3
        private const val ROWS_SIZE = 3
    }

    private lateinit var world: ConwayWorld

    @Before
    fun setup() {
        // The board is initialized as below:
        // [1, 1, 1]
        // [1, 0, 1]
        // [1, 1, 1]
        // Only the center cell isn't alive.
        world = ConwayWorld(
            COLUMN_SIZE,
            ROWS_SIZE,
            Array(COLUMN_SIZE) { i ->
                Array(ROWS_SIZE) { j ->
                    val isAlive = !(i == 1 && j == 1)
                    ConwayCell(i, j, isAlive)
                }
            }
        )
    }

    @Test
    fun testGetCell() {
        val cell = world.getCell(0, 0)
        assertEquals(0, cell?.xIndex)
        assertEquals(0, cell?.yIndex)
        assertEquals(true, cell?.isAlive)
    }

    @Test
    fun testGetAliveSiblingCount() {
        runBlocking {
            // Test (0,0)
            assertEquals(2, world.getAliveSiblingCount(0, 0))

            // Test (0,1)
            assertEquals(4, world.getAliveSiblingCount(0, 1))

            // Test (1,1)
            assertEquals(8, world.getAliveSiblingCount(1, 1))
        }
    }

    @Test
    fun testNextGeneration() {
        runBlocking {
            // Invoke nextGeneration() first
            world.nextGeneration()

            // Test (0,0)
            assertEquals(true, world.getCell(0, 0)?.isAlive)

            // Test (0,1)
            assertEquals(false, world.getCell(0, 1)?.isAlive)

            // Test (1,1)
            assertEquals(false, world.getCell(1, 1)?.isAlive)
        }
    }
}