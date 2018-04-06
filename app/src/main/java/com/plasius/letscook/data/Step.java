package com.plasius.letscook.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "steps")
public class Step implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    private int stepId;
    private String shortDescription;
    private String description;
    private String ThumbnailURL;
    private String VideoURL;



    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
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

    //PARCEL
    public Step(){}

    public Step(Parcel in){
        setId(in.readInt());
        setRecipeId(in.readInt());
        setStepId(in.readInt());
        setShortDescription(in.readString());
        setDescription(in.readString());
        setThumbnailURL(in.readString());
        setVideoURL(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getRecipeId());
        dest.writeInt(getStepId());
        dest.writeString(getShortDescription());
        dest.writeString(getDescription());
        dest.writeString(getThumbnailURL());
        dest.writeString(getVideoURL());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Step createFromParcel(Parcel in){
            return new Step(in);
        }

        public Step[] newArray(int size){
            return new Step[size];
        }
    };
}
