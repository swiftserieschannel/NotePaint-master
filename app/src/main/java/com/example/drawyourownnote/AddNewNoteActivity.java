package com.example.drawyourownnote;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewNoteActivity extends AppCompatActivity {


    ImageView imageViewEdit;
    EditText editTextTitle;
    EditText description;
    FloatingActionButton floatingActionButtonAddNote;
    Item mItem;
    int itemPosition;
    Boolean isExistingItem = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private JSONSerialLizer mSeriallizer;
    private ArrayList<Item> itemArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        mItem = new Item();
        imageViewEdit = (ImageView) findViewById(R.id. imageViewEdit);
        editTextTitle = (EditText) findViewById(R.id.paintView);
        description  = (EditText) findViewById(R.id.description);


        //file json, items is saved
        mSeriallizer = new JSONSerialLizer("DrawYourOwnNote.json",
                getApplicationContext());

        try {

            itemArrayList = mSeriallizer.load();

        } catch (Exception e) {

            itemArrayList = new ArrayList<Item>();
            Log.i("Error loading items: ", "Errrot Loading items");

        }

        // gets the selected Item from last activity
        Intent intent =  getIntent();
        if (intent != null){
            if (intent.getExtras() != null) {
                Item selectedItem = (Item) intent.getExtras().get("item");

                if (selectedItem != null) {

                    editTextTitle.setText(selectedItem.getTitle());
                    description.setText(selectedItem.getmDescription());
                    imageViewEdit.setImageURI(selectedItem.getImageUri());
                    imageViewEdit.setImageBitmap(Utile.getBitmapFromString(selectedItem.getImage()));
                    mItem = selectedItem;
                    itemPosition = intent.getIntExtra("position",0);
                    isExistingItem = true;
                    Log.e("Came from", "item received! For position" + itemPosition);

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

            String titleItemToAdd = editTextTitle.getText().toString();
            String descriptionItemToAddD = description.getText().toString();
            mItem.setTitle(titleItemToAdd);
            mItem.setDescription(descriptionItemToAddD);


        //picks the image from libary
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent imagePicker = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imagePicker, 1);

            }
        });



        //adds new note
        floatingActionButtonAddNote = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddPaint);
        floatingActionButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   String titleItemToAdd = editTextTitle.getText().toString();
                   String decriptionItemToAdd = description.getText().toString();
                   String image = Utile.convertImageToString(imageViewEdit.getDrawable());
                   System.out.println(decriptionItemToAdd + " ");
                   mItem.setTitle(titleItemToAdd);
                   mItem.setImage(image);
                   mItem.setDescription(decriptionItemToAdd);


                if (isExistingItem) {

                    itemArrayList.set(itemPosition, mItem);
                    Toast.makeText(AddNewNoteActivity.this, " You change note to " + titleItemToAdd, Toast.LENGTH_SHORT).show();

                }else {

                    itemArrayList.add(mItem);
                    Toast.makeText(AddNewNoteActivity.this, " You added a new note " + titleItemToAdd, Toast.LENGTH_SHORT).show();
                }



                    saveItems();
                    Log.d("Description", "onClick: item is going to be saved!");
                    finish();

            }

        });

    }



    //uploads images from gallery and selected that image
    public void SelectPhotoMethod(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Uri selectedImage = imageReturnedIntent.getData();
            mItem.setImageUri(selectedImage);
            imageViewEdit.setImageURI(selectedImage);
        }
    }



    //saves the items
    public void saveItems(){
        Log.d("Save Items()<-func : ", "saveItems: execution pointer is in saveItems");
        Log.e("Save Items()<-func", "SaveItems: total items " + itemArrayList.size() );
        try{

            mSeriallizer.save(itemArrayList);
            Toast.makeText(this, "Your last update is saved", Toast.LENGTH_SHORT).show();
            Log.i("SAVE New item it saved ", " ArrayList size" + String.valueOf(itemArrayList.size()));


        }catch(Exception e){
            Log.e("Error Saving Notes"," E.GetMassage() " + e.getMessage());
            Toast.makeText(this, "Your note is not saved, try again later!", Toast.LENGTH_SHORT).show();

        }


    }


    //it saves when the app is on pause
    protected void onPause(){
        super.onPause();

        saveItems();

    }


}
