package com.sstudio.conwaygame.quiz_two

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sstudio.conwaygame.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameOfLifeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.GameOfLiveViewStyle
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "GameOfLifeView"
        // Default values of custom attributes
        private const val DEFAULT_CELL_ALIVE_COLOR = Color.BLACK
        private const val DEFAULT_CELL_DEAD_COLOR = Color.WHITE
    }

    private lateinit var world: ConwayWorld

    // Variable for drawing
    private val rect = Rect()
    private val paint = Paint()

    // The rounds of generation in a second
    private var evolutionRate: Int
    // The milliseconds per generation last
    private var generationPeriod: Long

    // Custom attributes
    private var cellSize: Int
    private var aliveColor: Int
    private var deadColor: Int

    // Flag for playing / pausing game
    var isPlaying: Boolean = true
        private set

    init {
        // Get default integers
        evolutionRate = context.resources.getInteger(R.integer.default_evolution_rate)
        generationPeriod = 1000L / evolutionRate
        val defaultCellSize = context.resources.getInteger(R.integer.default_cell_size)

        // Get custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameOfLifeView, defStyleAttr, 0)
        aliveColor = typedArray.getColor(R.styleable.GameOfLifeView_cell_alive_color, DEFAULT_CELL_ALIVE_COLOR)
        deadColor = typedArray.getColor(R.styleable.GameOfLifeView_cell_dead_color, DEFAULT_CELL_DEAD_COLOR)
        cellSize = typedArray.getInt(R.styleable.GameOfLifeView_cell_size, defaultCellSize)
        typedArray.recycle()
    }

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

        // After draw, if it's still playing, switch to next generation.
        if (isPlaying) {
            switchToNextGeneration()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isPlaying) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Get the corresponding coordinates of cell based on touch event
                val xIndex = (event.x / cellSize).toInt()
                val yIndex = (event.y / cellSize).toInt()
                val cell = world.getCell(xIndex = xIndex, yIndex = yIndex)

                // Make the touched cell alive
                cell?.isAlive = true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * Pause game by changing [isPlaying] to false.
     */
    fun pause() {
        isPlaying = false
    }

    /**
     * Play game by changing [isPlaying] to true, and
     * invoke [switchToNextGeneration] to next generation.
     */
    fun play() {
        isPlaying = true
        // The previous generation is drawn already, so
        // here we can switch to next generation when continue.
        switchToNextGeneration()
    }

    /**
     * Set evolution rate dynamically.
     *
     * @param rate the rounds per second.
     */
    fun setEvolutionRate(rate: Int) {
        evolutionRate = rate
        generationPeriod = 1000L / evolutionRate
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
            if (isAttachedToWindow) {
                invalidate()
            }
        }
    }
}