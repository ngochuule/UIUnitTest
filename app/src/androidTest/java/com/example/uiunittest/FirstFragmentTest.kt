package com.example.uiunittest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29)
@LargeTest
class FirstFragmentTest  {
//    @Rule
//    @JvmField
//    val fragmentTestRule = FragmentTestRule

//    @Test
//    fun testNavigationToInSecondFragment() {
//        // Create a TestNavHostController
//        val navController = TestNavHostController(
//            ApplicationProvider.getApplicationContext())
//        navController.setGraph(R.navigation.nav_graph)
//
//        // Create a graphical FragmentScenario for the TitleScreen
//        val titleScenario = launchFragmentInContainer<FirstFragment>()
//
//        // Set the NavController property on the fragment
//        titleScenario.onFragment { fragment ->
//            Navigation.setViewNavController(fragment.requireView(), navController)
//        }
//
//        // Verify that performing a click changes the NavController’s state
//        onView(ViewMatchers.withId(R.id.button_first)).perform(ViewActions.click())
//        assertThat(navController.currentDestination?.id).isEqualTo(R.id.SecondFragment)
//    }

//    @Test
//    fun testSplashImageIsDisplayed() {
//        launchFragmentInContainer<FirstFragment>()
//
//        onView(withId(R.id.button_first)).check(matches(isDisplayed()))
//    }
}