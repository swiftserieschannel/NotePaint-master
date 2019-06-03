package com.example.drawyourownnote;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonPaint;
    private FloatingActionButton floatingActionButtonNote;
    private EditText editTextTitle;
    private ImageView imageViewEdit;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    //private ArrayList<Item> itemArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList itemArrayList;
    private JSONSerialLizer mSeriallizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeriallizer = new JSONSerialLizer("DrawYourOwnNote.json",
                getApplicationContext());



        editTextTitle = (EditText) findViewById(R.id.paintView);
        // when user clicks on floatbutton paint, it comes to paint activity
        floatingActionButtonPaint = (FloatingActionButton) findViewById(R.id.floatingActionButtonPaint);

        floatingActionButtonPaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddNewPaintActivity.class);
                intent.putExtra("items",itemArrayList);
                startActivityForResult(intent, -1);

            }
        });

        floatingActionButtonNote = (FloatingActionButton) findViewById(R.id.floatingActionButtonNote);

        //start activity addnewnote
        floatingActionButtonNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddNewNoteActivity.class);
                startActivityForResult(intent, -1);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        jsonLoad();
        setUpTheRecycler();
    }

    //set upp the recycler, layout and the adapter
    public void setUpTheRecycler(){


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new Adapter(MainActivity.this, itemArrayList);
        Log.e("Set Up Recycler View", "Sets Up The Recyclerview: " + itemArrayList.size());
       mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();

    }

    //code taht you can load up image
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageViewEdit.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageViewEdit.setImageURI(selectedImage);
                }
                break;
        }

    }

    //adds item
    public void  addItems(Item item){
        itemArrayList.add(item);
        mAdapter.notifyDataSetChanged();
        Log.e("SIZE of ARRAY","Size itemArrayList" + itemArrayList.size());


    }

//saving items

    public void saveItems(){
        Log.d("Save Items : ", "saveItems: execution pointer is in saveItems");
        try{

            mSeriallizer.save(itemArrayList);
            Log.e("Save", ""+ itemArrayList);


        }catch(Exception e){

            Log.e("Error Saving Notes","", e);
            Toast.makeText(MainActivity.this, "Error Saving Notes", Toast.LENGTH_SHORT).show();
        }


    }
    //picks upp the methods and class from json so it saves
    public void jsonLoad(){

        try {

            itemArrayList = mSeriallizer.load();
            Log.e("Load", "ArrayList" + itemArrayList);


        } catch (Exception e) {


            itemArrayList = new ArrayList<Item>();
            Log.i("Error loading items: ", "", e);
            Toast.makeText(this, "Error dosent loading items", Toast.LENGTH_SHORT).show();

        }

    }



}
