package me.kosert.githubusers.util

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.random.Random

@ColorInt
fun getRandomColor() : Int = Color.rgb(
    Random.nextInt(0, 255),
    Random.nextInt(0, 255),
    Random.nextInt(0, 255)
)