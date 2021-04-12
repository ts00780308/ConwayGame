package com.sstudio.conwaygame.quiz_one

import android.graphics.Rect

object RectUtil {

    /**
     * Check if the rectangles are overlapping. The method is referred to
     * [Rect.intersect].
     *
     * @param rect1 first rectangle.
     * @param rect2 second rectangle.
     */
    fun isRectOverlapping(rect1: Rect, rect2: Rect): Boolean {
        return rect1.left < rect2.right
            && rect2.left < rect1.right
            && rect1.top < rect2.bottom
            && rect2.top < rect1.bottom
    }
}