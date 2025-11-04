package com.example.cat_and_mise

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LevelsFragmentTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)
    }

    @After
    fun cleanup() {
        activityScenario.close()
    }

    @Test
    fun testLevelsListIsDisplayed() {
        // Переходим к фрагменту уровней
        Espresso.onView(ViewMatchers.withId(R.id.startButton))
            .perform(ViewActions.click())

        // Проверяем, что список уровней отображается
        Espresso.onView(ViewMatchers.withId(R.id.levelsRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testLevelsListContainsExpectedItems() {
        // Переходим к фрагменту уровней
        Espresso.onView(ViewMatchers.withId(R.id.startButton))
            .perform(ViewActions.click())

        Thread.sleep(500)

        // Проверяем наличие уровней в списке
        Espresso.onView(ViewMatchers.withText("Лесная опушка"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("Городские крыши"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("Подземелье"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}