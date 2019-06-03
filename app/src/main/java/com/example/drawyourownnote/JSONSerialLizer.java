package com.example.drawyourownnote;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class JSONSerialLizer {


    // filename where the data will be saved
    private String mFileName;
    //for writing data to a file.
    private Context mContext;


    //json contructor
    public JSONSerialLizer(String fn, Context context){


        mFileName = fn;
        mContext = context;
    }



    public void save(ArrayList<Item> itemList) throws IOException, JSONException {

        //makes an arraylist of json. ArrayList for handling JSON objects.
        JSONArray jsonArray = new JSONArray();

        //for loop to go through all the items objects in item and convert them to JSON objects using the convertToJSON method from the ItemAcitvity class
        for (Item items : itemList)

            jsonArray.put(items.convertToJSON());

        //write the data to an a file
        Writer writer = null;
        try {

            OutputStream outputStream = mContext.openFileOutput(mFileName, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(jsonArray.toString());

            Log.i("SAVEJSON", jsonArray.toString());

        }finally {

            if (writer != null){
                writer.close();
            }
        }

    }

    //opens and load and reads the data from file, takes the lenght of array

    public ArrayList<Item> load() throws  IOException, JSONException{


        ArrayList<Item> itemArrayList = new ArrayList<>();

        BufferedReader reader = null;

        try {
            InputStream inputStream = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();

            String line = null;

            Log.i("json seria load", "forsoker");

            while ((line = reader.readLine()) != null){

                jsonString.append(line);

            }
            Log.e("Load Data from file", "load: "+jsonString );
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jsonArray.length(); i++){
                itemArrayList.add(new Item( jsonArray.getJSONObject(i)));
                Log.i("json serializer LOAD", itemArrayList.get(i).getTitle());
                Log.i("json serializer LOAD", itemArrayList.get(i).getmDescription());
                Log.i("json serializer LOAD", String.valueOf(itemArrayList.size()));
            }


        }catch (FileNotFoundException e){

            Log.e("File Not Found : ", "load: JSON File not found");
        }finally {
            if (reader != null){
                reader.close();
            }
            return itemArrayList;
        }

    }
}
