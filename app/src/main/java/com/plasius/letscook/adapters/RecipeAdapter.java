package com.plasius.letscook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.plasius.letscook.MainActivity;
import com.plasius.letscook.R;
import com.plasius.letscook.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    private final List<Recipe> recipeList;
    private final Context context;
    private final OnItemClickListener listener;


    public RecipeAdapter(Context c, List<Recipe> recipes){
        recipeList = recipes;
        context = c;
        listener = (MainActivity) c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_name.setText(recipeList.get(position).getName());
        holder.tv_servings.setText(context.getString(R.string.servings) + Integer.toString(recipeList.get(position).getServings()));
        if(!recipeList.get(position).getImageURL().isEmpty())
            Picasso.get().load(recipeList.get(position).getImageURL()).into(holder.iv_image);


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //recipe indexes start at 1
                listener.onItemClick(position+1);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(recipeList != null)
        return recipeList.size();

        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tv_name;
        TextView tv_servings;
        ImageView iv_image;

        private ViewHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.main_tv_name);
            tv_servings = v.findViewById(R.id.main_tv_servings);
            iv_image = v.findViewById(R.id.main_iv_image);
        }

    }

}
