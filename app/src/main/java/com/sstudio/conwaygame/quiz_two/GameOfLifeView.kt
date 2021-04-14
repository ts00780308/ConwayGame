package com.sstudio.conwaygame.quiz_two

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameOfLifeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "GameOfLifeView"
        // Default edge size of conway cell
        private const val DEFAULT_EVOLUTION_PER_SECOND = 2
        private const val DEFAULT_CELL_SIZE = 30
        private const val DEFAULT_CELL_ALIVE_COLOR = Color.BLACK
        private const val DEFAULT_CELL_DEAD_COLOR = Color.WHITE
    }

    private lateinit var world: ConwayWorld

    private val rect = Rect()
    private val paint = Paint()

    // The rounds of generation in a second
    private var elevationRate: Int = DEFAULT_EVOLUTION_PER_SECOND
    // The milliseconds per generation last
    private var generationPeriod: Long = 1000L / elevationRate
    private var cellSize: Int = DEFAULT_CELL_SIZE
    private var aliveColor: Int = DEFAULT_CELL_ALIVE_COLOR
    private var deadColor: Int = DEFAULT_CELL_DEAD_COLOR

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Init world if haven't do so
        if (!::world.isInitialized) {
            initWorld(viewWidth = w, viewHeight = h)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw current generation to canvas
        drawCell(canvas)

        // After draw, switch to next generation
        switchToNextGeneration()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Get the corresponding coordinates of cell based on touch event
            val xIndex = (event.x / cellSize).toInt()
            val yIndex = (event.y / cellSize).toInt()
            val cell = world.getCell(xIndex = xIndex, yIndex = yIndex)

            // Make the touched cell alive
            cell?.isAlive = true
        }
        return super.onTouchEvent(event)
    }

    /**
     * Calculate count of column and row, and init [ConwayWorld].
     *
     * @param viewWidth the width of [GameOfLifeView].
     * @param viewHeight the height of [GameOfLifeView].
     */
    private fun initWorld(viewWidth: Int, viewHeight: Int) {
        // Calculate the number of columns and rows
        val columns = viewWidth / cellSize
        val rows = viewHeight / cellSize
        world = ConwayWorld(columns, rows, generateRandomBoard(columns, rows))
    }

    /**
     * Generate board fulfilled with random status cells.
     */
    private fun generateRandomBoard(columns: Int, rows: Int): Array<Array<ConwayCell>> {
        return Array(columns) { i ->
            Array(rows) { j ->
                ConwayCell(i, j, Random.nextBoolean())
            }
        }
    }

    /**
     * Draw cell to canvas according to the [ConwayCell.isAlive].
     */
    private fun drawCell(canvas: Canvas) {
        if (!::world.isInitialized) {
            throw Exception("The world hasn't initialized.")
        }

        for (i in 0 until world.columns) {
            for (j in 0 until world.rows) {
                rect.apply {
                    left = i * cellSize
                    right = i * cellSize + cellSize
                    top = j * cellSize
                    bottom = j * cellSize + cellSize
                }

                paint.color = if (world.getCell(i, j)?.isAlive == true) {
                    aliveColor
                } else {
                    deadColor
                }

                canvas.drawRect(rect, paint)
            }
        }
    }

    /**
     * Calculate cells status on next generation, and call [invalidate]
     * after
     */
    private fun switchToNextGeneration() {
        GlobalScope.launch {
            // Conduct next generation calculation
            world.nextGeneration()
            // Delay a while
            delay(generationPeriod)

            // Call invalidate() to draw next generation
            invalidate()
        }
    }
}