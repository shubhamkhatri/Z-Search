package com.example.android.zomatosearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.android.zomatosearch.R;
import com.example.android.zomatosearch.model.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantAdapter(@NonNull Context context, ArrayList<Restaurant> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_list_item, parent, false);
        }
        Restaurant currentList = getItem(position);
        TextView nameText = listItemView.findViewById(R.id.list_name);
        TextView idText = listItemView.findViewById(R.id.list_id);
        TextView cuisineText = listItemView.findViewById(R.id.list_cuisine);
        TextView addressText = listItemView.findViewById(R.id.list_address);
        TextView averageText=listItemView.findViewById(R.id.list_average);

        CardView cardView=listItemView.findViewById(R.id.list_cardView);
        RelativeLayout relativeLayout =listItemView.findViewById(R.id.list_relative_layout);
        TextView headerText=listItemView.findViewById(R.id.list_header);

          if(currentList.getName().equals("default")){
            cardView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            headerText.setText(currentList.getCuisines());
        }
        else {
            relativeLayout.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            nameText.setText(currentList.getName());
            idText.setText("ID: " + currentList.getId());
            cuisineText.setText("Cuisine: " + currentList.getCuisines());
            addressText.setText(currentList.getLocation().getAddress());
            averageText.setText("Average Price: " + currentList.getAverage_cost_for_two());
        }
        return listItemView;
    }
}
