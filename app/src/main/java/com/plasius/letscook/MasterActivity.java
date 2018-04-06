package com.plasius.letscook;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.plasius.letscook.adapters.OnItemClickListener;
import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.fragments.DetailFragment;
import com.plasius.letscook.fragments.MasterFragment;
import com.plasius.letscook.utils.PersistenceUtils;

import java.util.ArrayList;
import java.util.List;

public class MasterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean>, OnItemClickListener{
    private static final int MOVIE_LOADER_ID = 916;
    List<Step> stepList;
    List<Ingredient> ingredientList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("steps") && savedInstanceState.containsKey("ingredients")){
                stepList = savedInstanceState.getParcelableArrayList("steps");
                ingredientList = savedInstanceState.getParcelableArrayList("ingredients");
            }
        }

        initLoader();
    }

    private void initLoader(){
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);

    }


    //LOADER
    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new StepsAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        if(stepList == null || ingredientList == null)
            return;

        MasterFragment masterFragment = (MasterFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_master);
        masterFragment.loadSteps(stepList, ingredientList.size());

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


    }

    //pressed element in recyclerview
    @Override
    public void onItemClick(int position) {
        if(getResources().getBoolean(R.bool.isTablet)){
            //TODO adapt detailfragment

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
        outState.putParcelableArrayList("steps", new ArrayList<>(stepList));
        outState.putParcelableArrayList("ingredients", new ArrayList<>(ingredientList));
    }

}
