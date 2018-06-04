package edu.sjsu.ada.menuapp;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import edu.sjsu.ada.menuapp.dummy.DummyContent;
import edu.sjsu.ada.menuapp.dummy.DummyContent.DummyItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {OnListFragmentInteractionListener}
 * interface.
 */
public class R_ListFragment extends ListFragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    ArrayList<Recipe> allRecipes;
    ArrayList<String> itemList = new ArrayList<>();
    ArrayList<String> selectedItemList = new ArrayList<>();

    ArrayAdapter<String> adapter;

    ListView recipeListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public R_ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        // use adapter
        adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, itemList);
        setListAdapter(adapter);

        return inflater.inflate(R.layout.fragment_list_layout, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "On long click listener", Toast.LENGTH_LONG).show();
                Intent editRecipeActivity = new Intent(getActivity().getApplicationContext(), NewDishActivity.class);
                editRecipeActivity.putExtra("recipeName", getListAdapter().getItem(position).toString());
                startActivity(editRecipeActivity);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //recipeListView = (ListView) view.findViewById(R.id.list);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
        String item = (String) getListAdapter().getItem(position);
        //Toast.makeText(getContext(), item + " added.", Toast.LENGTH_SHORT).show();

        selectedItemList.add(item);

        for(Recipe x : allRecipes) {
            if(item.equals(x.getName())) {
                x.increaseCount();
                Toast.makeText(getContext(), item + " added.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        FileOutputStream ofile = null;
        ObjectOutputStream out = null;
        File file = new File(getContext().getFilesDir(), "data.dat");

        try {
            ofile = new FileOutputStream(file);
            out = new ObjectOutputStream(ofile);
            out.writeObject(allRecipes);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.v("Save error: ", e.getMessage());
            e.printStackTrace();
        }
    }
}
