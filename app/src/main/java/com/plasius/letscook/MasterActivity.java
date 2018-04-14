package com.plasius.letscook;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.plasius.letscook.adapters.OnItemClickListener;
import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.fragments.DetailFragment;
import com.plasius.letscook.fragments.MasterFragment;
import com.plasius.letscook.utils.PersistenceUtils;
import com.plasius.letscook.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

public class MasterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean>, OnItemClickListener{
    private static final int MOVIE_LOADER_ID = 916;
    int currentStep;
    List<Step> stepList;
    List<Ingredient> ingredientList;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
                currentStep = savedInstanceState.getInt("currentStep");
                stepList = savedInstanceState.getParcelableArrayList("steps");
                ingredientList = savedInstanceState.getParcelableArrayList("ingredients");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        //set title
        ((TextView)findViewById(R.id.toolbar_tv_title)).setText(PersistenceUtils.getSharedPrefString(this, "currentRecipeName", "Current Recipe"));
        //we opened a recipe, let's make this the current one
        RecipeWidgetProvider.sendRefreshBroadcast(this);

        initLoader();
    }

    private void initFragment(){
        DetailFragment detailFragment = DetailFragment.newInstance(stepList, ingredientList, currentStep);

        if(getSupportFragmentManager().findFragmentByTag("fragment-detail") != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();
        else
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();

    }




    private void initLoader(){
        LoaderManager loaderManager = getSupportLoaderManager();
        if(loaderManager.getLoader(MOVIE_LOADER_ID) == null)
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        else
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);

    }


    //LOADER
    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new StepsAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }

    static class StepsAsyncLoader extends AsyncTaskLoader<Boolean>{
        MasterActivity context;

        private StepsAsyncLoader(Context context) {
            super(context);
            this.context = (MasterActivity) context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public Boolean loadInBackground() {
            int recipeId = PersistenceUtils.getSharedPrefInt(context, "currentRecipe", 1);

            context.ingredientList = AppDatabase.getAppDatabase(context).ingredientDAO().getByRecipeId(recipeId);

            context.stepList = AppDatabase.getAppDatabase(context).stepDAO().getByRecipeId(recipeId);
            context.stepList.get(0).setDescription("Ingredients needed: " + context.ingredientList.size());

            return true;
        }

        @Override
        public void deliverResult(Boolean data) {
            super.deliverResult(data);


            if(context.stepList == null || context.ingredientList == null)
                return;
            MasterFragment masterFragment = (MasterFragment) context.getSupportFragmentManager().findFragmentById(R.id.fragment_master);
            masterFragment.loadSteps(context.stepList, context.ingredientList.size(), context.currentStep);


            if(context.getResources().getBoolean(R.bool.isTablet))
                context.initFragment();
        }
    }

    //pressed element in recyclerview
    @Override
    public void onItemClick(int position) {
        if(getResources().getBoolean(R.bool.isTablet)){
            currentStep = position;

            DetailFragment detailFragment = DetailFragment.newInstance(stepList, ingredientList, currentStep);

            if(getSupportFragmentManager().findFragmentByTag("fragment-detail") != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_framelayout, detailFragment, "fragment-detail").commit();


        }else{
            //we are on mobile launch a DetailActivity
            Intent intent = new Intent(this, DetailActivity.class);

            intent.putExtra("currentStep", position);
            intent.putParcelableArrayListExtra("steps", new ArrayList<>(stepList));
            intent.putParcelableArrayListExtra("ingredients", new ArrayList<>(ingredientList));

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentStep", currentStep);
        outState.putParcelableArrayList("steps", new ArrayList<>(stepList));
        outState.putParcelableArrayList("ingredients", new ArrayList<>(ingredientList));
    }

}
