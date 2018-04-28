package com.debdroid.bakingapp.ui;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.debdroid.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeRestOfActivitiesTest {

    private static final String RECIPE_INGREDIENT = "Recipe Ingredients";

    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void restOfActivities_RecipeIngredientTest() {
        // Click on a recipe
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Check that RecipeDetailActivity is displayed
        onView(withId(R.id.fl_recipe_detail_fragment_container)).check(matches(isDisplayed()));

        // Now Click on a recipe detail adapter at position 0 which contains the ingredient
        onView(withId(R.id.rv_recipe_step_description_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        // Check that RecipeStepDetailActivity is displayed
        onView(withId(R.id.rv_recipe_ingredient_list)).check(matches(isDisplayed()));
        // Check the text 'Recipe Ingredient' is displayed
        onView(withText(RECIPE_INGREDIENT)).check(matches(isDisplayed()));
    }

    @Test
    public void restOfActivities_RecipeStepDetailActivityTest() {
        // Click on a recipe
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Check that RecipeDetailActivity is displayed
        onView(withId(R.id.fl_recipe_detail_fragment_container)).check(matches(isDisplayed()));

        // Now Click on a recipe detail adapter item
        onView(withId(R.id.rv_recipe_step_description_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        // Check that RecipeStepDetailActivity is displayed
        onView(withId(R.id.recipe_step_media_container)).check(matches(isDisplayed()));
    }
}
