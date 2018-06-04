package edu.sjsu.ada.menuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NewDishActivity extends AppCompatActivity {
    EditText recipeText;
    EditText directionsText;

    ImageView recipeImgView;
    Button addImgBtn;
    private static int RESULT_LOAD_IMAGE = 1;
    private boolean defaultImageUsed = true;


    String recipeName;
    String directions;
    byte[] byteArray = null;

    ArrayList<Recipe> allRecipes = new ArrayList<>();
    ArrayList<String> allIngredients = new ArrayList<>();

    Button saveBtn;

    Recipe editRecipe;
    boolean recipeExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        setTitle("Add a new dish");

        recipeText = (EditText) findViewById(R.id.recipeNameField);
        recipeImgView = (ImageView) findViewById(R.id.defaultRecipeImg);
        addImgBtn = (Button) findViewById(R.id.addButton);
        directionsText = (EditText) findViewById(R.id.cookingDirections);
        saveBtn = (Button) findViewById(R.id.saveButton);

        addImgBtn.setOnClickListener(getImage);
        saveBtn.setOnClickListener(saveRecipe);

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

        // Edit mode from R_ListFragment.java
        Intent intent = getIntent();
        if(intent.hasExtra("recipeName")) {
            Toast.makeText(NewDishActivity.this, "recipe exists", Toast.LENGTH_SHORT).show();

            String rName = intent.getStringExtra("recipeName");

            for(Recipe x : allRecipes) {
                if(rName.equals(x.getName())) {
                    editRecipe = x;

                    recipeExists = true;

                    // set recipe name
                    recipeText.setText(editRecipe.getName());
                    // set image
                    byteArray = editRecipe.getByteArray();
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    recipeImgView.setImageBitmap(bmp);
                    // set desc
                    directionsText.setText(editRecipe.getDescription());
                    // set ingredients
                    for(int i = 1; i <= editRecipe.getIngrNames().size(); i++) {
                        EditText itemText = (EditText) findViewById(getResources().getIdentifier("item"+i, "id", getPackageName()));
                        EditText qtyText = (EditText) findViewById(getResources().getIdentifier("qty"+i, "id", getPackageName()));
                        Spinner unitSpinner = (Spinner) findViewById(getResources().getIdentifier("unitspinner"+i, "id", getPackageName()));

                        itemText.setText(editRecipe.getIngrNames().get(i-1));
                        qtyText.setText(editRecipe.getQty().get(i-1));
                        unitSpinner.setSelection(((ArrayAdapter)unitSpinner.getAdapter()).getPosition(editRecipe.getUnits().get(i-1)));
                    }
                }
            }
        }

        // check name if recipe doesnt exist
        recipeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!recipeExists) {
                        for (Recipe x : allRecipes) {
                            if (x.getName().equals(recipeText.getText().toString())) {
                                recipeText.setError("Recipe already exists. Please change name.");
                            }
                        }
                    }
                }
            }
        });

        // populate the ingredient name spinners
        for(Recipe x : allRecipes) {
            for(String ingr : x.getIngrNames()) {
                if(!allIngredients.contains(ingr)) {
                    allIngredients.add(ingr);
                }
            }
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allIngredients);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        for(int i = 1; i <= 10; i++) {
            final EditText itemText = (EditText) findViewById(getResources().getIdentifier("item"+i, "id", getPackageName()));
            final Spinner itemSpinner = (Spinner) findViewById(getResources().getIdentifier("itemspinner"+i, "id", getPackageName()));
            itemSpinner.setAdapter(spinnerArrayAdapter);
            itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int check = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(++check > 1) {
                        itemText.setText(itemSpinner.getSelectedItem().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    // recipe image pt 1
    private final View.OnClickListener getImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imgIntent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(imgIntent, "Select an image."), RESULT_LOAD_IMAGE);

        }
    };
    // recipe image pt 2
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            // set image
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                recipeImgView.setImageBitmap(bitmap);
                defaultImageUsed = false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // save image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteArray = stream.toByteArray();
        }
    };

    // saving the recipe
    private final View.OnClickListener saveRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // bootleg update
            if(recipeExists) {
                int index = allRecipes.indexOf(editRecipe);
                allRecipes.remove(index);
            }

            recipeName = recipeText.getText().toString();
            directions = directionsText.getText().toString();
            //Toast.makeText(NewDishActivity.this, recipeName, Toast.LENGTH_SHORT).show();

            ArrayList<String> allItems = new ArrayList<>();
            ArrayList<String> allQty = new ArrayList<>();
            ArrayList<String> allUnits = new ArrayList<>();

            for(int i = 1; i <= 10; i++) {
                EditText itemText = (EditText) findViewById(getResources().getIdentifier("item"+i, "id", getPackageName()));
                EditText qtyText = (EditText) findViewById(getResources().getIdentifier("qty"+i, "id", getPackageName()));
                Spinner unitSpinner = (Spinner) findViewById(getResources().getIdentifier("unitspinner"+i, "id", getPackageName()));

                if(!itemText.getText().toString().trim().isEmpty() && !qtyText.getText().toString().isEmpty()) {
                    String itemStr = itemText.getText().toString();
                    String qtyNum = qtyText.getText().toString();
                    String unitStr = unitSpinner.getSelectedItem().toString();
                    allItems.add(itemStr);
                    allQty.add(qtyNum);
                    allUnits.add(unitStr);
                }
            }

            // if no recipe image is selected, save default image
            if(defaultImageUsed) {
                Uri defaultImgUri = Uri.parse("android.resource://edu.sjsu.ada.menuapp/" + R.drawable.default_newrecipe_icon);
                Bitmap defaultImgBitmap = null;
                try {
                    defaultImgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), defaultImgUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                defaultImgBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byteArray = stream.toByteArray();
            }


            Recipe newDish = new Recipe(recipeName, allItems, allQty, allUnits, directions, byteArray);
                //Toast.makeText(NewDishActivity.this, newDish.getIngredients(), Toast.LENGTH_SHORT).show();

            allRecipes.add(newDish);
                //Toast.makeText(NewDishActivity.this, allRecipes.toString(), Toast.LENGTH_SHORT).show();

            FileOutputStream ofile = null;
            ObjectOutputStream out = null;
            File file = new File(getApplicationContext().getFilesDir(), "data.dat");

            try {
                ofile = new FileOutputStream(file);
                out = new ObjectOutputStream(ofile);
                out.writeObject(allRecipes);
                out.flush();
                out.close();
                if(recipeExists) {
                    Toast.makeText(NewDishActivity.this, "Recipe updated. Return to Main Screen.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewDishActivity.this, "Recipe saved. Return to Main Screen.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.v("Save error: ", e.getMessage());
                e.printStackTrace();
                Toast.makeText(NewDishActivity.this, "Recipe not saved. :(", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void returnHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
