package edu.sjsu.ada.menuapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ada on 3/8/18.
 */

public class Recipe implements Serializable {
    private String name;
    private ArrayList<String> ingredients;
    private ArrayList<String> qty;
    private ArrayList<String> units;
    private String instructions;
    private byte[] img;
    private int count;


    public Recipe(String name, ArrayList<String> ingredients, ArrayList<String> qty, ArrayList<String> units, String instructions, byte[] img) {
        this.name = name;
        this.ingredients = ingredients;
        this.qty = qty;
        this.units = units;
        this.instructions = instructions;
        this.img = img;
        this.count = 0;
    }


    public String getName() {
        return this.name;
    }

    public byte[] getByteArray() {return this.img; }

    public String getDescription() { return this.instructions; }


    public ArrayList<String> getIngrNames() { return this.ingredients; }

    public ArrayList<String> getQty() { return this.qty; }

    public ArrayList<String> getUnits() { return this.units; }


    public void increaseCount() { this.count++; }

    public void decreaseCount() { this.count--; }

    public int getCount() { return this.count; }


}
