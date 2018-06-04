package edu.sjsu.ada.menuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GroceriesActivity extends AppCompatActivity {

    private static final String LOG_TAG = GroceriesActivity.class.getSimpleName();

    private ListView recipeListView;

    private ArrayList<Recipe> allRecipes;
    ArrayList<Recipe> newRecipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        setTitle("Groceries list");

        // deserialize
        File file = new File(getApplicationContext().getFilesDir(), "data.dat");

        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            allRecipes = (ArrayList<Recipe>)oi.readObject();
            oi.close();
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Recipe x : allRecipes) {
            for (int i = 0; i < x.getIngrNames().size(); i++) {
                newRecipeList.add(x);
                Log.v(LOG_TAG ,"recipe added: " + x.getName());
            }
        }

        recipeListView = (ListView) findViewById(R.id.groceriesListView);

        GroceriesAdapter groceriesAdapter = new GroceriesAdapter(getApplicationContext(), R.layout.groceries_list_item, newRecipeList);
        recipeListView.setAdapter(groceriesAdapter);
    }
}
