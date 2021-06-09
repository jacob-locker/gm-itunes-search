package com.jlocker.itunesartists

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException


object RecyclerViewMatchers {
    @JvmStatic
    fun hasItemCount(itemCount: Int, greaterThan: Boolean = false): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(
            RecyclerView::class.java
        ) {

            override fun describeTo(description: Description) {
                description.appendText("has $itemCount items")
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                if (greaterThan) {
                    return view.adapter?.itemCount ?: 0 > itemCount
                }
                return view.adapter?.itemCount == itemCount
            }
        }
    }

    @JvmStatic
    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}

object ViewActions {
    @JvmStatic
    fun waitForVisibility(viewId: Int, millis: Long, visibility: Int) = object: ViewAction {
        override fun getConstraints(): Matcher<View> {
            return any(View::class.java)
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadUntilIdle()
            val startTime: Long = System.currentTimeMillis()
            val endTime: Long = startTime + millis

            do {
                if (view?.visibility == visibility) {
                    return
                }

                uiController?.loopMainThreadForAtLeast(10)
            }
            while (System.currentTimeMillis() < endTime)

            throw PerformException.Builder()
                .withActionDescription(description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }

    @JvmStatic
    fun waitForItems(viewId: Int, millis: Long) = object: ViewAction {
        override fun getConstraints(): Matcher<View> {
            return any(View::class.java)
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadUntilIdle()
            val startTime: Long = System.currentTimeMillis()
            val endTime: Long = startTime + millis

            require(view is RecyclerView) {
                "waitForItems only works with a recycler view."
            }

            do {
                if (view.adapter?.itemCount ?: 0 > 0) {
                    return
                }

                uiController?.loopMainThreadForAtLeast(10)
            }
            while (System.currentTimeMillis() < endTime)

            throw PerformException.Builder()
                .withActionDescription(description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }

    @JvmStatic
    fun orientationLandscape(): ViewAction {
        return OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    @JvmStatic
    fun orientationPortrait(): ViewAction {
        return OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }
}

private class OrientationChangeAction(private val orientation: Int): ViewAction {

    override fun getDescription(): String = "change orientation to $orientation"

    override fun getConstraints(): Matcher<View> = isRoot()

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        var activity = getActivity(view.context)
        if (activity == null && view is ViewGroup) {
            val c = view.childCount
            var i = 0
            while (i < c && activity == null) {
                activity = getActivity(view.getChildAt(i).context)
                ++i
            }
        }
        activity!!.requestedOrientation = orientation
    }

    private fun getActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = (context as ContextWrapper).baseContext
        }
        return null
    }

}