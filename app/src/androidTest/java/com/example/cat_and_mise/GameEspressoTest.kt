package com.example.cat_and_mise

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.cat_and_mise.game.GameService
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class GameEspressoTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        GameService.stop(ApplicationProvider.getApplicationContext())
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
        GameService.stop(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testGameStartFlow() {
        // Проверяем стартовый экран
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.startButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        // Проверяем экран выбора уровня
        Espresso.onView(ViewMatchers.withText("Выберите уровень"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Выбираем первый уровень
        Espresso.onView(ViewMatchers.withText("Лесная опушка"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        // Проверяем что появилась панель выбранного уровня
        Espresso.onView(ViewMatchers.withId(R.id.selectedLevelInfo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Проверяем кнопку запуска игры
        Espresso.onView(ViewMatchers.withId(R.id.startGameButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testSettingsPersistence() {
        // Переходим в настройки
        Espresso.onView(ViewMatchers.withId(R.id.imageSetting))
            .perform(ViewActions.click())

        // Меняем имя игрока
        Espresso.onView(ViewMatchers.withId(R.id.playerNameEditText))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("TestPlayer"))

        // Выключаем музыку
        Espresso.onView(ViewMatchers.withId(R.id.musicSwitch))
            .perform(ViewActions.click())

        // Возвращаемся назад
        Espresso.onView(ViewMatchers.withId(R.id.backButton))
            .perform(ViewActions.click())

        // Снова заходим в настройки и проверяем сохранение
        Espresso.onView(ViewMatchers.withId(R.id.imageSetting))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.playerNameEditText))
            .check(ViewAssertions.matches(ViewMatchers.withText("TestPlayer")))

        Espresso.onView(ViewMatchers.withId(R.id.musicSwitch))
            .check(ViewAssertions.matches(ViewMatchers.isNotChecked()))
    }

    @Test
    fun testLeaderboardNavigation() {
        // Проверяем навигацию к таблице рекордов
        Espresso.onView(ViewMatchers.withId(R.id.leaderboardButton))
            .perform(ViewActions.click())

        // Проверяем что попали на экран таблицы рекордов
        Espresso.onView(ViewMatchers.withText("Таблица рекордов"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.leaderboardRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testLevelsListDisplay() {
        // Переходим к выбору уровней
        Espresso.onView(ViewMatchers.withId(R.id.startButton))
            .perform(ViewActions.click())

        // Проверяем что отображаются уровни
        Espresso.onView(ViewMatchers.withText("Лесная опушка"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("Городские крыши"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("Подземелье"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}