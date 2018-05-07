package com.plasius.letscook;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.fragments.DetailFragment;
import com.plasius.letscook.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

//fragmentocmpatactivity?

/*
* Activity opened only from mobile
* Hosts 1 DetailFragment
*/

public class DetailActivity extends AppCompatActivity {
    int currentStep;
    List<Step> stepList;
    List<Ingredient> ingredientList;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            stepList = savedInstanceState.getParcelableArrayList("steps");
            ingredientList = savedInstanceState.getParcelableArrayList("ingredients");
            currentStep = savedInstanceState.getInt("currentStep", 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(!NetworkUtils.isNetworkAvailable(this))
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();


        if(stepList == null){
            stepList = getIntent().getParcelableArrayListExtra("steps");
            ingredientList = getIntent().getParcelableArrayListExtra("ingredients");
            currentStep = getIntent().getIntExtra("currentStep", 0);
        }

        //first time
        if(savedInstanceState == null) {
            //initialize fragment
            initFragment();
        }
    }

    private void initFragment(){
        DetailFragment detailFragment = DetailFragment.newInstance(stepList, ingredientList, currentStep);

        if(getSupportFragmentManager().findFragmentByTag("fragment-detail") != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();
        else
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();


    }

    public void backButtonPressed(View v){
        if(currentStep>0) {
            currentStep--;
            initFragment();
        }else{
            Toast.makeText(this, "It all starts here.", Toast.LENGTH_SHORT).show();
        }
    }

    public void forwardButtonPressed(View v){
        if(currentStep<stepList.size()-1){
            currentStep++;
            initFragment();
        }else{
            Toast.makeText(this, "You are all done!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ingredients", new ArrayList<>(ingredientList));
        outState.putParcelableArrayList("steps", new ArrayList<>(stepList));
        outState.putInt("currentStep", currentStep);
    }



}
