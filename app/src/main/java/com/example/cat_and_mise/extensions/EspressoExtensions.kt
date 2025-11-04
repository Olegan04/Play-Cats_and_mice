package com.example.cat_and_mise.extensions

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.util.HumanReadables

fun ViewInteraction.withTimeout(millis: Long): ViewInteraction {
    return this.check { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val endTime = System.currentTimeMillis() + millis
        while (System.currentTimeMillis() < endTime) {
            if (view.isShown) {
                return@check
            }
            Thread.sleep(100)
        }

        throw Exception("View not found within timeout: ${HumanReadables.describe(view)}")
    }
}