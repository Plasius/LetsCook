package com.plasius.letscook;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.plasius.letscook.adapters.RecipeAdapter;
import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.data.Recipe;
import com.plasius.letscook.data.Step;
import com.plasius.letscook.utils.PersistenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean>, RecipeAdapter.OnItemClickListener{
    private static final int MOVIE_LOADER_ID = 647;
    private static final int LOAD_RECIPES = 511;
    private static final int FETCH_RECIPES = 939;

    @BindView(R.id.main_rv_recipes) RecyclerView recyclerView;

    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //if theres no data
        if(!PersistenceUtils.getSharedPrefBool(this, "data",false)) {
            initLoader(FETCH_RECIPES);
        }else{
            //load the data
            initLoader(LOAD_RECIPES);
        }


    }

    //populates RecyclerView with the recipes from DB
    private void loadView(){
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    //LOADER
    private void initLoader(int whatToDo) {
        Bundle bundle = new Bundle();
        bundle.putInt("whatToDo", whatToDo);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(MOVIE_LOADER_ID, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, this);
        }

    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new RecipesAsyncLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }



    static class RecipesAsyncLoader extends AsyncTaskLoader<Boolean> {
        MainActivity context;
        Bundle args;

        //ct
        private RecipesAsyncLoader(MainActivity c, Bundle data){
            super(c);
            args= data;
            context= c;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public Boolean loadInBackground() {
            if(args.getInt("whatToDo") == LOAD_RECIPES){
                context.recipeList = AppDatabase.getAppDatabase(context).recipeDAO().getAll();

                return true;
            }

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                if(scanner.hasNext()){
                    return processResult(scanner.next());
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return false;
        }

        private boolean processResult(String data){
            try{
                JSONArray jArray = new JSONArray(data);
                
                Recipe tempRecipe;
                JSONObject recipeObj;
                JSONObject stepObj;
                JSONArray steps;
                JSONArray ingredients;
                
                for(int i=0; i<jArray.length(); i++){
                    //recipes
                    tempRecipe = new Recipe();
                    recipeObj = jArray.getJSONObject(i);

                    tempRecipe.setId(recipeObj.getLong("id"));
                    tempRecipe.setName(recipeObj.getString("name"));
                    tempRecipe.setServings(recipeObj.getInt("servings"));
                    tempRecipe.setImageURL(recipeObj.getString("image"));

                    AppDatabase.getAppDatabase(context).recipeDAO().insertAll(tempRecipe);
                    
                    //steps
                    steps= recipeObj.getJSONArray("steps");
                    Step tempStep;

                    for(int j=0; j<steps.length(); j++){
                        tempStep = new Step();
                        stepObj = steps.getJSONObject(j);

                        tempStep.setStepId(stepObj.getInt("id"));
                        tempStep.setShortDescription(stepObj.getString("shortDescription"));
                        tempStep.setDescription(stepObj.getString("description"));
                        tempStep.setVideoURL(stepObj.getString("videoURL"));
                        tempStep.setThumbnailURL(stepObj.getString("thumbnailURL"));
                        tempStep.setRecipeId(tempRecipe.getId());
                        
                        AppDatabase.getAppDatabase(context).stepDAO().insertAll(tempStep);
                    }
                    
                    //ingredients
                    ingredients= recipeObj.getJSONArray("ingredients");
                    Ingredient tempIngredient;

                    for(int j=0; j<ingredients.length(); j++){
                        tempIngredient = new Ingredient();
                        stepObj = ingredients.getJSONObject(j);

                        tempIngredient.setQuantity(stepObj.getInt("quantity"));
                        tempIngredient.setMeasure(stepObj.getString("measure"));
                        tempIngredient.setName(stepObj.getString("ingredient"));
                        tempIngredient.setRecipeId(tempRecipe.getId());

                        AppDatabase.getAppDatabase(context).ingredientDAO().insertAll(tempIngredient);
                    }
                    
                    
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        public void deliverResult(Boolean gotData) {
            if(args.getInt("whatToDo") == LOAD_RECIPES)
                context.loadView();
            else if(gotData){
                PersistenceUtils.setSharedPrefBool(context, "data", true);
                context.loadView();
            }

        }
    }

    //recyclerview onitemclicklistener
    @Override
    public void onItemClick(int recipeID) {
        PersistenceUtils.setSharedPrefInt(this, "currentRecipe", recipeID);

        Intent i = new Intent(this, MasterActivity.class);
        startActivity(i);
    }
}
