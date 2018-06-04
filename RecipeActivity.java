package edu.sjsu.ada.menuapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            R_DetailFragment r_DetailFragment = new R_DetailFragment();
            fragmentTransaction.replace(android.R.id.content, r_DetailFragment);
        } else {
            R_ListFragment r_ListFragment = new R_ListFragment();
            fragmentTransaction.replace(android.R.id.content, r_ListFragment);
        }

        fragmentTransaction.commit();
    }
}
