package edu.sjsu.ada.menuapp;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class R_DetailFragment extends Fragment {


    ArrayList<Recipe> allRecipes;
    ArrayList<String> itemList = new ArrayList<>();

    private ListView recipeListView;

    private String selectedItem;
    private Recipe selectedRecipe;



    public R_DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Recipes");

        // deserialize
        File file = new File(getContext().getFilesDir(), "data.dat");

        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            allRecipes = (ArrayList<Recipe>)oi.readObject();
            oi.close();
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add to itemList
        for(Recipe r : allRecipes) {
            itemList.add(r.getName());
            //Toast.makeText(getActivity(), r.getName(), Toast.LENGTH_SHORT).show();
        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecipeListView();
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recipeListView = (ListView) v.findViewById(R.id.listView);
        loadRecipeListView();
    }

    private void loadRecipeListView() {
        recipeListView = (ListView) getView().findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.activity_list_view, R.id.textView, itemList);
        recipeListView.setAdapter(arrayAdapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRecipe = allRecipes.get(position);
                setRecipeData(selectedRecipe);
            }
        });

        recipeListView.setLongClickable(true);
        recipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editRecipeActivity = new Intent(getActivity().getApplicationContext(), NewDishActivity.class);
                editRecipeActivity.putExtra("recipeName", allRecipes.get(position).getName());
                startActivity(editRecipeActivity);
                return true;
            }
        });
    }

    private void setRecipeData(Recipe recipe) {
        TextView recipeName = (TextView) getView().findViewById(R.id.recipeName);
        ImageView recipeImage = (ImageView) getView().findViewById(R.id.recipeImage);
        TextView recipeIngr = (TextView) getView().findViewById(R.id.recipeIngredients);
        TextView recipeDesc = (TextView) getView().findViewById(R.id.recipeDescription);

        // set name
        recipeName.setText(recipe.getName());
        // set image
        byte[] byteArray = recipe.getByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        recipeImage.setImageBitmap(bmp);
        // set ingredient list
        StringBuffer buf = new StringBuffer("");
        for(int i = 0; i < recipe.getIngrNames().size(); i++) {
            buf.append("* ");
            buf.append(recipe.getIngrNames().get(i));
            buf.append(" (");
            buf.append(recipe.getQty().get(i));
            buf.append(" ");
            buf.append(recipe.getUnits().get(i));
            buf.append(") \n");
        }
        recipeIngr.setText(buf.toString());
        // set description
        recipeDesc.setText(recipe.getDescription());
    }



}
