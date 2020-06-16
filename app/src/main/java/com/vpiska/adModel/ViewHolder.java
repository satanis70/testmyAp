package com.vpiska.adModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vpiska.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView Recycyler_name_ad, Recycyler_Time, AdLayoutName;
    ImageView adImage;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        adImage = itemView.findViewById(R.id.addImage);
        Recycyler_name_ad = itemView.findViewById(R.id.Recycyler_name_ad);
        Recycyler_Time = itemView.findViewById(R.id.Recycyler_Time);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;

    }
}
