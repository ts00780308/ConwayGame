package com.sstudio.conwaygame.quiz_two

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Conway’s Game of Life is based on 4 rules:
 * 1. Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
 * 2. Any live cell with two or three live neighbors lives on to the next generation.
 * 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
 * 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 */
class ConwayWorld(
    val columns: Int,
    val rows: Int,
    private val board: Array<Array<ConwayCell>>
) {
    /**
     * Get cell by x and y index.
     *
     * @param xIndex the horizontal index.
     * @param yIndex the vertical index.
     * @return the target cell if inputs are valid, else return null.
     */
    fun getCell(xIndex: Int, yIndex: Int): ConwayCell? {
        if (xIndex >= columns || xIndex < 0) {
            return null
        }

        if (yIndex >= rows || yIndex < 0) {
            return null
        }

        return board[xIndex][yIndex]
    }

    /**
     * Switch to next generation by going through all cells, and change their status
     * base on 4 rules listed on the top.
     */
    suspend fun nextGeneration() = withContext(Dispatchers.IO) {
        // The reborn and dead cells in the next generation
        val rebornCells = mutableListOf<ConwayCell>()
        val deadCells = mutableListOf<ConwayCell>()

        for (i in 0 until columns) {
            for (j in 0 until rows) {
                val cell = board[i][j]
                val aliveSiblings = getAliveSiblingCount(cell.xIndex, cell.yIndex)

                // rule 1 & rule 3
                if (cell.isAlive && (aliveSiblings < 2 || aliveSiblings > 3)) {
                    deadCells.add(cell)
                }

                // rule 4
                if (!cell.isAlive && aliveSiblings == 3) {
                    rebornCells.add(cell)
                }
            }
        }

        // The cell status, which meet rule 2, doesn't change, so skip it.
        // Go through the rebornCells and deadCells, calling the responding
        // method.
        for (c in rebornCells) {
            c.reborn()
        }

        for (c in deadCells) {
            c.die()
        }
    }

    /**
     * Go through cells around the target coordinate, and
     * count alive siblings.
     *
     * @param xIndex the horizontal index.
     * @param yIndex the vertical index.
     */
    @VisibleForTesting
    suspend fun getAliveSiblingCount(
        xIndex: Int,
        yIndex: Int
    ): Int = withContext(Dispatchers.IO) {
        var count = 0
        for (i in xIndex-1..xIndex+1) {
            for (j in yIndex-1..yIndex+1) {
                if ((i != xIndex || j != yIndex)
                    && i >= 0 && i < columns
                    && j >= 0 && j < rows
                ) {
                    if (board[i][j].isAlive) {
                        count++
                    }
                }
            }
        }

        return@withContext count
    }
}