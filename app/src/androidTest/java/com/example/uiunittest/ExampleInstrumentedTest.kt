package com.example.uiunittest

import android.Manifest
import android.util.Log
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.example.uiunittest.uiUtils.ScreenShotTakingRule
import com.example.uiunittest.uiUtils.UiTestUtils
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.Parameterized
import java.util.regex.Matcher

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29)
@LargeTest
class ExampleInstrumentedTest {

    private val packageName = "com.example.uiunittest"
    private val mUIUtils: UiTestUtils = UiTestUtils()
    private val TAG ="ExampleInstrumentedTest"

    @Rule
    @JvmField
    val mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

//    @Rule
//    @JvmField
//    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
//        Manifest.permission.CAMERA,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )

    @Rule
    @JvmField
    val mActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    val screenShotRule = ScreenShotTakingRule(this.mUIUtils)


    @Before
    fun setup() {
//        mActivityTestRule.activity.supportFragmentManager.beginTransaction()
//        mActivityTestRule.activity.set
        this.mUIUtils.setActivity(mActivityTestRule.activity)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun useAppContext() {
        Log.e("UITest","useAppContext")
        this.mUIUtils.prepareScreenShot()
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(packageName, appContext.packageName)
        this.mUIUtils.sleep("SHR")
        this.mUIUtils.removeSuccessScreenShots()
    }

    @Test
    fun checkFloatingActionButton() {
        this.mUIUtils.prepareScreenShot()
        mUIUtils.screenShot("", "BEFORE >>> Fab Button click")
//        Espresso.onView(ViewMatchers.withId(R.id.button_first)).check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(""))))
//        onView(withId(R.id.button_first)).check(matches(withText(containsString(""))))
        onView(withId(R.id.fab)).perform(click())
//        mUIUtils.allowPermissionsIfNeeded()
        mUIUtils.screenShot("", "AFTER >>> Fab Button click")
        val snackBarTapped = Matchers.allOf(
            withId(R.id.snackbar_text),
            withText("Replace with your own action")
        )
        onView(snackBarTapped).check(matches(isDisplayed()))
        this.mUIUtils.sleep("SHR")
        this.mUIUtils.removeSuccessScreenShots()
    }

    private fun waitForSnackbarDisappear(targetMatcher: org.hamcrest.Matcher<View>) {
        var doLoop = true
        while(doLoop) {
            try {
                onView(targetMatcher).check(matches(isDisplayed()))
                this.mUIUtils.sleep("SHR")
            } catch(e: NoMatchingViewException) {
                Log.e(TAG,"This error is telling test runner that there are no snackbar on screen.")
                doLoop = false
            }
        }
    }


    @Test
    fun testFloatingActionButtonIsDisplayed() {
        this.mUIUtils.prepareScreenShot()
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        this.mUIUtils.sleep("SHR")
        this.mUIUtils.removeSuccessScreenShots()
    }
}