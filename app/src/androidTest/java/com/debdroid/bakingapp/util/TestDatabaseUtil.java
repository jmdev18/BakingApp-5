package com.debdroid.bakingapp.util;

import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.database.RecipeEntity;
import com.debdroid.bakingapp.database.StepEntity;

import java.util.Arrays;
import java.util.List;

public class TestDatabaseUtil {

    public static final RecipeEntity RECIPE_ENTITY_1 = new RecipeEntity(1,"recipe 1",
            4,"image 1");
    public static final RecipeEntity RECIPE_ENTITY_2 = new RecipeEntity(2,"recipe 2",
            6,"image 2");
    public static final List<RecipeEntity> RECIPE_ENTITY = Arrays.asList(RECIPE_ENTITY_1, RECIPE_ENTITY_2);

    public static final IngredientEntity INGREDIENT_ENTITY_1 =
            new IngredientEntity(1,1,2.0,"Cup","Sugar");
    public static final IngredientEntity INGREDIENT_ENTITY_2 =
            new IngredientEntity(2,1,1.0,"Quantity","Egg");
    public static final IngredientEntity INGREDIENT_ENTITY_3 =
            new IngredientEntity(3,2,5.0,"Cup","Flour");
    public static final List<IngredientEntity> INGREDIENT_ENTITY = Arrays.asList(INGREDIENT_ENTITY_1,
            INGREDIENT_ENTITY_2, INGREDIENT_ENTITY_3);

    public static final StepEntity STEP_ENTITY_1 = new StepEntity(1,1,1,
            "short desc 1","desc 1","video url 1","thumb url 1");
    public static final StepEntity STEP_ENTITY_2 = new StepEntity(2,1,2,
            "short desc 2","desc 2","video url 2","thumb url 2");
    public static final StepEntity STEP_ENTITY_3 = new StepEntity(3,2,1,
            "short desc1","desc 1","video url 1","thumb url 1");
    public static final List<StepEntity> STEP_ENTITY = Arrays.asList(STEP_ENTITY_1, STEP_ENTITY_2, STEP_ENTITY_3);
}
