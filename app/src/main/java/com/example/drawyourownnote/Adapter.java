package com.example.drawyourownnote;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ListItemHolder> {

    //private ArrayList<Item> itemList;
    private Context context;
    private List<Item> mItemList;
    private MainActivity mMainActivity;


    public Adapter(MainActivity mainActivity,
                   List<Item> itemlist) {

        mMainActivity = mainActivity;
        mItemList = itemlist;
    }


    @NonNull
    @Override
    public Adapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);


        return new ListItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ListItemHolder listItemHolder, final int position) {
        final Item item = mItemList.get(position);
        listItemHolder.mTitle.setText(item.getTitle());
        // set image
        listItemHolder.mImage.setImageBitmap(Utile.getBitmapFromString(item.getImage()));
        // set the complete item to list Item Holder so that on click we can pass whole to another activity
        listItemHolder.item = item;
        listItemHolder.position = position;
        Log.d("Tag", "onBindViewHolder: -----------"+item.getmDescription());

        //     listItemHolder.mImage.setImageURI(item.getImageUri());




        listItemHolder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent intent = new Intent(v.getContext(), NewItem.class);


                TextView textView = (TextView) listItemHolder.mTitle.findViewById(R.id.textViewTitle);
                ImageView imageView = (ImageView) listItemHolder.mImage.findViewById(R.id.imageViewEdit);
                Intent intent = new Intent(mMainActivity, AddNewNoteActivity.class);

                intent.putExtra("textViewTitle", textView.getText().toString());
                Toast.makeText(mMainActivity, "You clicked on image "  + String.valueOf(textView.getText()), Toast.LENGTH_SHORT).show();

                intent = new Intent(v.getContext(), AddNewNoteActivity.class);
                intent.putExtra("item",item);
                intent.putExtra("position",position);
                if (item == null){
                    Log.d("onClick", "onClick: item is found null");
                }
                mMainActivity.startActivity(intent);


            }
        });



    }


    @Override
    public int getItemCount() {

        return mItemList.size();
    }


    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mTitle;
        ImageView mImage;
        Item item;
        int position;

        public ListItemHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = (TextView)
                    itemView.findViewById(R.id.textViewTitle);
            mImage = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
            //mImage.setOnClickListener(this);
            //mTitle.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            Log.i("info", "" + getAdapterPosition());
            Toast.makeText(mMainActivity, "You clicked on image " + " " + String.valueOf(mTitle.getText() ), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AddNewNoteActivity.class);
            intent.putExtra("item",item);
            intent.putExtra("position",position);
            if (item == null){
                Log.d("onClick", "onClick: item is found null");
            }
            Item it = (Item)intent.getExtras().get("item");
            Log.d("Test-----------", "" + it.getmDescription());
            mMainActivity.startActivity(intent);

        }
    }
}