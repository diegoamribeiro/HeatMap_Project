package com.dmribeiro87.poc_heatmap.utils

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

class PolygonColors {

    private fun generateTransparentColor(color: Int, alpha: Double?): Int {
        val defaultAlpha = 255 // (0 - Invisible / 255 - Max visibility)
        val colorAlpha = alpha?.times(defaultAlpha)?.roundToInt() ?: defaultAlpha
        return ColorUtils.setAlphaComponent(color, colorAlpha)
    }

    fun scoreToFillColor(score: Int): Int {
        return when (score) {
            in 31..100000 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.7)
            }
            in 26..30 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.6)
            }
            in 21..25 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.5)
            }
            in 16..20 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.4)
            }
            in 11..15 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.3)
            }
            in 6..10 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.2)
            }
            in 1..5 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.1)
            }
            in -5..-1 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.1)
            }
            in -10..-6 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.2)
            }
            in -15..-11 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.3)
            }
            in -20..-16 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.4)
            }
            in -25..-21 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.5)
            }
            in -30..-26 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.6)
            }
            in -100000..-31 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.7)
            }
            else -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.0)
            }
        }
    }

    fun scoreToStrokeColor(score: Int): Int {
        return when (score) {
            in 1..31 -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.8)
            }
            in -100000..-1 -> {
                generateTransparentColor(Color.parseColor("#0000cb"), 0.8)
            }
            else -> {
                generateTransparentColor(Color.parseColor("#cb0000"), 0.0)
            }
        }
    }

}