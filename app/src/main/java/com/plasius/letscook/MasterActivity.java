package com.plasius.letscook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.plasius.letscook.utils.PersistenceUtils;

public class MasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        if(getIntent()!=null)
            Toast.makeText(this,
                    Integer.toString(PersistenceUtils.getSharedPrefInt(this, "currentRecipe", 0)),
                    Toast.LENGTH_SHORT).show();
    }
}
