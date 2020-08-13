package com.vpiska.adModel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vpiska.Activities.AdActivity;
import com.vpiska.Activities.Home;
import com.vpiska.R;

import java.util.List;

public class CustomAdapterRecycler extends RecyclerView.Adapter<ViewHolder> {

    Home home;
    List<ModelAd> modelAdList;
    ImageView imageView;
    public String name_ad,Time, more_Details,User;
    public CustomAdapterRecycler( Home home, List<ModelAd> modelAdList) {
        this.home = home;
        this.modelAdList = modelAdList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_model_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        imageView = itemView.findViewById(R.id.addImage);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                name_ad = modelAdList.get(position).getName_Ads();
                Time = modelAdList.get(position).getTime();
                User = modelAdList.get(position).getUser();
                Intent intent = new Intent(view.getContext(), AdActivity.class);
                intent.putExtra("Name_Ads", modelAdList.get(position).getName_Ads());
                intent.putExtra("adImage", modelAdList.get(position).getAdImage());
                intent.putExtra("More_Details", modelAdList.get(position).getMore_Details());
                intent.putExtra("Phone", modelAdList.get(position).getPhone());
                intent.putExtra("User", modelAdList.get(position).getUser());
                view.getContext().startActivity(intent);
                Toast.makeText(home, name_ad + "\n" + Time, Toast.LENGTH_SHORT).show();
                //view.getContext().startActivity(new Intent(view.getContext(), AdActivity.class));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelAd mList = modelAdList.get(position);
        Picasso.get().load(mList.getAdImage()).resize(350,350).into(holder.adImage);
        holder.Recycyler_name_ad.setText(modelAdList.get(position).getName_Ads());
        holder.Recycyler_Time.setText(String.valueOf(modelAdList.get(position).getTime()));



    }

    @Override
    public int getItemCount() {
        return modelAdList.size();
    }
}
