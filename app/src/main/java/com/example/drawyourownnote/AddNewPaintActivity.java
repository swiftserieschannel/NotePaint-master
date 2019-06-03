package com.example.drawyourownnote;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewPaintActivity extends AppCompatActivity {

    private PaintView paintView;
    private FloatingActionButton saveBtn;
    private JSONSerialLizer mSeriallizer;
    private ArrayList<Item> alreadySaveItems;
    Item mItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_paint);

       // PaintView paintView = new PaintView(this);

        // initialization of instance variables

        // initialization of paint view
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        mItem = new Item();
        saveBtn = (FloatingActionButton)findViewById(R.id.floatingActionButtonAddPaint);
        mSeriallizer = new JSONSerialLizer("DrawYourOwnNote.json",
                getApplicationContext());



        getItemsFromLastActivity();



        // on clicking of save just save the data to json file
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paintConvertedString = Utile.convertBitmapToString(paintView.mBitmap);
                mItem.setTitle("Undefined");
                mItem.setDescription("This dummy description to save pain");
                mItem.setImage(paintConvertedString);
                alreadySaveItems.add(mItem);
                saveItems();
                finish();
            }
        });

    }

    void getItemsFromLastActivity(){
        // gets the selected Item from last activity
        Intent intent =  getIntent();
        if (intent != null){
            if (intent.getExtras() != null) {
                ArrayList<Item> allItems = (ArrayList<Item>) intent.getExtras().get("items");

                if (allItems != null) {
                        alreadySaveItems = allItems;
//                    editTextTitle.setText(selectedItem.getTitle());
//                    description.setText(selectedItem.getmDescription());
//                    imageViewEdit.setImageURI(selectedItem.getImageUri());
//                    imageViewEdit.setImageBitmap(Utile.getBitmapFromString(selectedItem.getImage()));
//                    mItem = selectedItem;
//                    itemPosition = intent.getIntExtra("position",0);
//                    isExistingItem = true;
//                    Log.e("Came from", "item received! For position" + itemPosition);

                } else {
                    Log.e("previous item ", "item not received!");
                }
            }

            else {

                mItem = new Item();
                Log.e("new item initialized ","item not received!");
            }

        }else {

            mItem = new Item();
            Log.e("new item initialized ","item not received!");
        }
    }


    //saves the items
    public void saveItems(){
        Log.d("Save Items()<-func : ", "saveItems: execution pointer is in saveItems");
        Log.e("Save Items()<-func", "SaveItems: total items " + alreadySaveItems.size() );
        try{

            mSeriallizer.save(alreadySaveItems);
            Toast.makeText(this, "Your last update is saved", Toast.LENGTH_SHORT).show();
            Log.i("SAVE New item it saved ", " ArrayList size" + String.valueOf(alreadySaveItems.size()));


        }catch(Exception e){
            Log.e("Error Saving Notes"," E.GetMassage() " + e.getMessage());
            Toast.makeText(this, "Your note is not saved, try again later!", Toast.LENGTH_SHORT).show();

        }


    }
}
