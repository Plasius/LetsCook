package com.plasius.letscook;


import android.os.SystemClock;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)
public class MainActivityRecipeTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    //check inter-ingredient navigation
    @Test
    public void checkStepNavigation(){
        onView(withId(R.id.main_rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //ingredients are always the first item
        onView(withId(R.id.fragment_master))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        onView(withText("NEXT STEP")).perform(ViewActions.scrollTo());
        onView(withText("NEXT STEP")).perform(click());

        onView(withId(R.id.fragment_detail_tv_description)).check(matches(withText(containsString("1."))));
    }


    //check if we need eggs for the cheesecake
    @Test
    public void checkIngredientInRecipe(){

        //I hope we have a cheesecake
        onView(withId(R.id.main_rv_recipes))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Cheesecake")), click()));


        //ingredients are always the first item
        onView(withId(R.id.fragment_master))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        onView(withId(R.id.fragment_detail_tv_description)).check(matches(withText(containsString("eggs"))));
    }

}
