package com.debdroid.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient", foreignKeys = @ForeignKey(entity = RecipeEntity.class,
                                                                parentColumns = "id",
                                                                childColumns = "recipe_id",
                                                                onDelete = ForeignKey.CASCADE),
                                    indices = {@Index("recipe_id")})
public class IngredientEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipe_id;

    @ColumnInfo(name = "quantity")
    public double quantity;

    @ColumnInfo(name = "measure")
    public String measure;

    @ColumnInfo(name = "ingredient")
    public String ingredient;

    public IngredientEntity() {}

    public IngredientEntity(int id, int recipe_id, double quantity, String measure, String ingredient) {
        this.id = id;
        this.recipe_id = recipe_id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
