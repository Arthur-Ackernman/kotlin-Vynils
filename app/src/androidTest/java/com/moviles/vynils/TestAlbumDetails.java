package com.moviles.vynils;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.moviles.vynils.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestAlbumDetails {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAlbumDetails() throws InterruptedException {
        Thread.sleep(5000);
        //Click on albums
        onView(withId(R.id.home_album_image)).perform(click());
        Thread.sleep(2000);
        int i=0;
        boolean thereAlbums=false;
        while (true){
            try {
                onView(withId(R.id.albumsRv)).perform(RecyclerViewActions.actionOnItemAtPosition(i,click()));
                Thread.sleep(2000);

                //Validate detail view is displayed - HU02
                onView(allOf(withId(R.id.titleAlbum))).check(matches(isDisplayed()));
                Espresso.pressBack();
                Thread.sleep(1000);
                i++;
                thereAlbums=true;
            }catch (PerformException e){
                break;
            }
            if (i==50){
                break;
            }

        }
        assertEquals(true, thereAlbums);
    }
}