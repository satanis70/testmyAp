package com.vpiska.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.vpiska.Chat.ChatActivity;
import com.vpiska.R;
import com.vpiska.adModel.CustomAdapterRecycler;
import com.vpiska.adModel.ModelAd;

import java.util.List;

public class AdActivity extends AppCompatActivity {


    ImageView ad_image;
    TextView ad_name, more_details;
    CustomAdapterRecycler customAdapterRecycler;
    List<ModelAd> modelAdList;
    ModelAd modelAd;
    Button buttonChat;
    String user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_layout);
        ad_image = findViewById(R.id.ad_layout_image_Open);
        ad_name = findViewById(R.id.ad_layout_name_Open);
        more_details = findViewById(R.id.more_Details_Open);
        buttonChat = findViewById(R.id.button_open_chat);
        String adName = getIntent().getStringExtra("Name_Ads");
        String imgurl = getIntent().getStringExtra("adImage");
        String more_Details = getIntent().getStringExtra("More_Details");
        user = getIntent().getStringExtra("User");
        Picasso.get().load(imgurl).into(ad_image);
        ad_name.setText(adName);
        more_details.setText(more_Details);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdActivity.this, ChatActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });




    }
}
