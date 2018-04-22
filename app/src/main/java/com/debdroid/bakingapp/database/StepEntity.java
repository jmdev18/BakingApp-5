package com.debdroid.bakingapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step", foreignKeys = @ForeignKey(entity = RecipeEntity.class,
                                                                parentColumns = "id",
                                                                childColumns = "recipe_id",
                                                                onDelete = ForeignKey.CASCADE),
                            indices = {@Index("recipe_id")})

public class StepEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipe_id;

    @ColumnInfo(name = "step_id")
    public int stepId;

    @ColumnInfo(name = "short_description")
    public String shortDescription;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "video_url")
    public String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    public String thumbnailURL;

    public StepEntity() {}

    public StepEntity(int id, int recipe_id, int stepId, String shortDescription, String description,
                      String videoURL, String thumbnailURL) {
        this.id = id;
        this.recipe_id = recipe_id;
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }
}
