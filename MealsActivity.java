package edu.sjsu.ada.menuapp;

import edu.sjsu.ada.menuapp.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class MealsActivity extends AppCompatActivity {

    private ArrayList<Recipe> allRecipes;
    private ArrayList<Recipe> availableRecipes = new ArrayList<>();
    private ArrayList<String> availNames = new ArrayList<>();

    ArrayAdapter<String> spinAdapter;

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        setTitle("Meal Planning");

        // read in recipes
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

        // default option
        availNames.add("Eating out");

        // add recipes * counts to list
        for(Recipe r : allRecipes) {
            int count = r.getCount();
            if(count > 0) {
                for(int i = 0; i < count; i++) {
                    availableRecipes.add(r);
                    availNames.add(r.getName());
                }
            }
        }

        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, availNames);
        spinAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        for(int i = 1; i <= 7; i++) {
            final Spinner br = (Spinner) findViewById(getResources().getIdentifier("breakfast"+i, "id", getPackageName()));
            final Spinner lu = (Spinner) findViewById(getResources().getIdentifier("lunch"+i, "id", getPackageName()));
            final Spinner di = (Spinner) findViewById(getResources().getIdentifier("dinner"+i, "id", getPackageName()));

            // breakfast, lunch, dinner
            br.setAdapter(spinAdapter);
            lu.setAdapter(spinAdapter);
            di.setAdapter(spinAdapter);


            br.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!br.getSelectedItem().toString().equals("Eating out"))
                    removeItem(br.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            lu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!lu.getSelectedItem().toString().equals("Eating out"))
                    removeItem(lu.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            di.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!di.getSelectedItem().toString().equals("Eating out"))
                    removeItem(di.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

         }

        ll = (LinearLayout) findViewById(R.id.linearLayout);
        Button btn = (Button) findViewById(R.id.goalBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo();
            }
        });

    }

    private void displayInfo() {
        LayoutInflater inflater = (LayoutInflater) MealsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_meals, null);

        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, 1100, 1500, true);
        //Button to close the pop-up
        Button closeBtn = (Button) layout.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        pw.showAtLocation(ll, Gravity.CENTER,0,0);
    }

    public void removeItem(String item) {
        for(int i = 0; i < availNames.size(); i++) {
            if(availNames.get(i).equals(item)) {
                availNames.remove(i);
                break;
            }
        }
    }


}
