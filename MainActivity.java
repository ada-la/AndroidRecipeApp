package edu.sjsu.ada.menuapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.rootLayout);

        ImageView banner = (ImageView) findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo();
            }
        });
    }

    private void displayInfo() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup, null);

        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, 1000, 800, true);
        //Button to close the pop-up
        Button closeBtn = (Button) layout.findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        pw.showAtLocation(linearLayout, Gravity.CENTER,0,0);
    }

    public void mealsButton(View view) {
        Intent intent = new Intent(this, MealsActivity.class);
        startActivity(intent);

    }
    public void recipeButton(View view) {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }
    public void groceriesButton(View view) {
        Intent intent = new Intent(this, GroceriesActivity.class);
        startActivity(intent);
    }
    public void addDishButton(View view) {
        Intent intent = new Intent(this, NewDishActivity.class);
        startActivity(intent);
    }
}
