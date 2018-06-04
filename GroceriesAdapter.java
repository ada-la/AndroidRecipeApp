package edu.sjsu.ada.menuapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ada on 3/18/18.
 */

public class GroceriesAdapter extends ArrayAdapter implements ListAdapter {
    private ArrayList<Recipe>  allRecipes;
    private ArrayList<Recipe> recipeList;

    private ArrayList<String> ingrList;
    private ArrayList<String> qtyList;
    private ArrayList<String> unitList;

    private Context context;



    public GroceriesAdapter(Context context, int textViewResourceId, ArrayList<Recipe> allRecipes) {
        super(context, textViewResourceId, allRecipes);
        this.allRecipes = allRecipes;
        this.context = context;

        /*// deserialize
        File file = new File(context.getFilesDir(), "data.dat");
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            allRecipes = (ArrayList<Recipe>)oi.readObject();
            oi.close();
            fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        ingrList = new ArrayList<>();
        qtyList = new ArrayList<>();
        unitList = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.groceries_list_item, null);
        }

        recipeList = new ArrayList<>();
        for(Recipe x : allRecipes) {
            if(!recipeList.contains(x)) {
                recipeList.add(x);
            }
        }

        for (Recipe x : recipeList) {
            for(String z : x.getIngrNames()) {
                if (!ingrList.contains(z)) {
                    for (String ingr : x.getIngrNames()) {
                        ingrList.add(ingr);
                    }
                    for (String ingr : x.getQty()) {
                        qtyList.add(ingr);
                    }
                    for (String ingr : x.getUnits()) {
                        unitList.add(ingr);
                    }
                } /*else {
                    int index = ingrList.indexOf(z);
                    int oldVal = Integer.parseInt(qtyList.get(position));
                    int valToAdd = Integer.parseInt(x.getQty().get(index));
                    String added = Integer.toString(oldVal + valToAdd);
                    qtyList.set(position, added);
                }*/
            }
        }

        final TextView listItem = (TextView) view.findViewById(R.id.list_item);
        listItem.setText(displayItem(position));

        if(Integer.parseInt(qtyList.get(position)) <= 0 ) {
            listItem.setPaintFlags(listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        final GestureDetector gestureDetector = new GestureDetector(context, new MyGestureDetector(position, view));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        Button minusBtn = (Button) view.findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyList.get(position));
                if(qty > 1) {
                    qtyList.set(position, Integer.toString(qty-1));
                    listItem.setPaintFlags(0);
                    listItem.setText(displayItem(position));
                } else {
                    listItem.setPaintFlags(listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    qtyList.set(position, Integer.toString(0));
                }
                listItem.setText(displayItem(position));
            }
        });

        Button plusBtn = (Button) view.findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyList.get(position));
                qtyList.set(position, Integer.toString(qty+1));
                listItem.setPaintFlags(0);
                listItem.setText(displayItem(position));
            }
        });

        return view;
    }

    public String displayItem(int n) {
        StringBuffer buf = new StringBuffer("");
        buf.append(ingrList.get(n));
        buf.append(" (");
        buf.append(qtyList.get(n));
        buf.append(" ");
        buf.append(unitList.get(n));
        buf.append(")");

        return buf.toString();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        int position = 0;
        View view;

        public MyGestureDetector(int position, View view) {
            this.position = position;
            this.view = view;
        }

        /**
         * Notified of a fling event when it occurs with the initial on down {@link MotionEvent}
         * and the matching up {@link MotionEvent}. The calculated velocity is supplied along
         * the x and y axis in pixels per second.
         *
         * @param e1        The first down motion event that started the fling.
         * @param e2        The move motion event that triggered the current onFling.
         * @param velocityX The velocity of this fling measured in pixels per second
         *                  along the x axis.
         * @param velocityY The velocity of this fling measured in pixels per second
         *                  along the y axis.
         * @return true if the event is consumed, else false
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() >= 0) {
                // display btns
                View buttonLayout = view.findViewById(R.id.btns);
                if(!buttonLayout.isShown()) buttonLayout.setVisibility(View.VISIBLE);
                return true;
            }  else {
                // hide btns
                View buttonLayout = view.findViewById(R.id.btns);
                if(buttonLayout.isShown()) buttonLayout.setVisibility(View.GONE);
                return true;
            }
        }
    }
}
