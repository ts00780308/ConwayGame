package com.sstudio.conwaygame.quiz_one

import android.graphics.Rect
import io.mockk.spyk
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RectUtilTest {

    @Test
    fun testIsRectIntersected() {
        // Test true case
        val spyRect1 = getSpyRect(0, 0, 2, 4)
        val spyRect2 = getSpyRect(1, 3, 5, 5)
        assertTrue(RectUtil.isRectOverlapping(spyRect1, spyRect2))

        // Test false case
        val spyRect3 = getSpyRect(0, 0, 1, 1)
        val spyRect4 = getSpyRect(2, 2, 3, 3)
        assertFalse(RectUtil.isRectOverlapping(spyRect3, spyRect4))
    }

    /**
     * Generate spy [Rect] for unit testing.
     */
    private fun getSpyRect(left: Int, top: Int, right: Int, bottom: Int): Rect {
        return spyk<Rect>().apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }
    }
}