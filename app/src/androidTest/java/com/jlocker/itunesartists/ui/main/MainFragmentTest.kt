package com.jlocker.itunesartists.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.jlocker.itunesartists.MainActivity
import com.jlocker.itunesartists.R
import com.jlocker.itunesartists.RecyclerViewMatchers
import com.jlocker.itunesartists.RecyclerViewMatchers.atPosition
import com.jlocker.itunesartists.ViewActions.orientationLandscape
import com.jlocker.itunesartists.ViewActions.waitForItems
import com.jlocker.itunesartists.ViewActions.waitForVisibility
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testDefaultDisplay() {

        onView(withId(R.id.etSearchTracks))
            .check(matches(isDisplayed()))
            .check(matches(isFocused()))

        onView(withId(R.id.btnSearch))
            .check(matches(isDisplayed()))

        onView(withId(R.id.progress_bar))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.rvTracks)).check(matches(RecyclerViewMatchers.hasItemCount(0)))
    }

    @Test
    fun testProgressBarFunctionality() {
        testSearch("The Killers", testProgressBar = true)
    }

    fun testSearch(searchTerm: String, testProgressBar: Boolean = false, expectedItemCount: Int = 10) {
        onView(withId(R.id.etSearchTracks))
            .perform(replaceText(searchTerm))
        onView(withId(R.id.etSearchTracks))
            .check(matches(withText(searchTerm)))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btnSearch))
            .perform(click())

        if (testProgressBar) {
            onView(withId(R.id.progress_bar)).perform(waitForVisibility(R.id.progress_bar, 10000, View.VISIBLE))
        }

        onView(withId(R.id.rvTracks)).perform(waitForItems(R.id.rvTracks, 10000))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rvTracks))
            .check(matches(RecyclerViewMatchers.hasItemCount(expectedItemCount, greaterThan = true)))
            .check(matches(hasDescendant(withText(searchTerm))))
    }

    @Test
    fun testSearchResultsSurviveConfigChange() {
        testSearch("Born of Osiris")

        val recyclerItemCount = getRecyclerItemCount()
        assert(recyclerItemCount > 0) {
            "Recycler View had no items after searching."
        }

        onView(isRoot()).perform(orientationLandscape())

        assert(recyclerItemCount == getRecyclerItemCount()) {
            "Recycler View did not persist through configuration changes to landscape."
        }
    }

    @Test
    fun testMultipleSearches() {
        testSearch("Michael Jackson")
        testSearch("Lady Gaga")
    }

    @Test
    fun testNonExactSearch() {
        val searchTerm = "bob"
        val resultPosition = 0
        val artistSearchResult = "Bob Marley & The Wailers"
        val trackSearchResult = "Three Little Birds"
        val dateSearchResult = "1984-05-08"
        val genreSearchResult = "Reggae"
        val priceSearchResult = "1.29"

        onView(withId(R.id.etSearchTracks))
            .perform(replaceText(searchTerm))
        onView(withId(R.id.etSearchTracks))
            .check(matches(withText(searchTerm)))

        onView(withId(R.id.btnSearch))
            .perform(click())

        Thread.sleep(5000)
        onView(withId(R.id.rvTracks))
            .check(matches(RecyclerViewMatchers.hasItemCount(10, greaterThan = true)))
            .check(matches(atPosition(resultPosition, hasDescendant(withText(artistSearchResult)))))
            .check(matches(atPosition(resultPosition, hasDescendant(withText(trackSearchResult)))))
            .check(matches(atPosition(resultPosition, hasDescendant(withText(dateSearchResult)))))
            .check(matches(atPosition(resultPosition, hasDescendant(withText(genreSearchResult)))))
            .check(matches(atPosition(resultPosition, hasDescendant(withText(priceSearchResult)))))
    }

    private fun getRecyclerItemCount(): Int {
        return activityTestRule.activity.findViewById<RecyclerView>(R.id.rvTracks).adapter?.itemCount ?: 0
    }
}