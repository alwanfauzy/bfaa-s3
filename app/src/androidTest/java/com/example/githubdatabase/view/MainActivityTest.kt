package com.example.githubdatabase.view

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubdatabase.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyUsername = "alwan"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun actionBar() {
        onView(withId(R.id.menu_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText(dummyUsername))
        onView(withId(R.id.search_src_text)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.search_close_btn)).perform(click())
        onView(withId(R.id.menu_favorite)).perform(click())
        onView(isRoot()).perform(pressBack())
        onView(withId(R.id.menu_settings)).perform(click())
        onView(isRoot()).perform(pressBack())
    }

    @Test
    fun recyclerView() {
        onView(withId(R.id.rv_search)).check(matches(isDisplayed()))
    }
}