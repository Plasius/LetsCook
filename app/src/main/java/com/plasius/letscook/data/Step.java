package com.plasius.letscook.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

@Entity(tableName = "steps")
public class Step {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long recipeId;
    private int stepId;
    private String shortDescription;
    private String description;
    private String ThumbnailURL;
    private String VideoURL;



    public void setId(long id) {
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return ThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    @Dao
    public interface StepDAO{
        @Query("SELECT * FROM steps WHERE recipeId=:recipeId")
        List<Step> getByRecipeId(long recipeId);

        @Insert
        void insertAll(Step... steps);

        @Query("DELETE FROM steps")
        void deleteAll();
    }
}
