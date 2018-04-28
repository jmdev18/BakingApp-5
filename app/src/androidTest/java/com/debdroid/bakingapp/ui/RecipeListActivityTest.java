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
public class RecipeListActivityTest {

    private static final String RECIPE_NAME_NUTELLA_PIE = "Nutella Pie";
    private static final String RECIPE_INGREDIENT = "Recipe Ingredients";

    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void basicRecipeListActivityTest() {
        //Check the RecipeListActivity opens
        onView(withId(R.id.fl_recipe_list_fragment_container)).check(matches(isDisplayed()));

        //Check that recipe is displayed
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(RECIPE_NAME_NUTELLA_PIE)).check(matches(isDisplayed()));
    }


    @Test
    public void clickRecipeListGridItem_OpensRecipeDetailActivity() {
        // Click on second recipe
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        // Check that RecipeDetailActivity is displayed
        onView(withId(R.id.tb_recipe_detail_toolbar)).check(matches(isDisplayed()));

        // Check that Recipe Ingredients is displayed as first item in the adapter
        onView(withId(R.id.rv_recipe_step_description_list)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(RECIPE_INGREDIENT)).check(matches(isDisplayed()));
    }
}
