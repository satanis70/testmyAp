package com.vpiska.UserChatModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vpiska.Chat.ChatMessage;
import com.vpiska.Chat.UserMessagesActivity;
import com.vpiska.Home;
import com.vpiska.R;
import com.vpiska.adModel.ModelAd;
import com.vpiska.adModel.ViewHolder;

import java.util.List;

public class UserMassagesRecyclerAdapter extends RecyclerView.Adapter<ViewHolderChat>{
    UserMessagesActivity userMessagesActivity;
    List<UserChatsModel> userChatsModels;
    ImageView imageView;
    String autor;
    public UserMassagesRecyclerAdapter(UserMessagesActivity userMessagesActivity, List<UserChatsModel> userChatsModels) {
        this.userMessagesActivity = userMessagesActivity;
        this.userChatsModels = userChatsModels;
    }
    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_layout_item, parent, false);
        ViewHolderChat viewHolder = new ViewHolderChat(itemView);
        imageView = itemView.findViewById(R.id.Recycyler_ChatImageUser);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                autor = userChatsModels.get(position).getUserName();
                Toast.makeText(userMessagesActivity,autor, Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {
        UserChatsModel muserChatsModel = userChatsModels.get(position);
        Picasso.get().load(muserChatsModel.getUserImg()).resize(350,350).into(holder.imageViewChatUser);
        holder.Recycyler_nameUser.setText(userChatsModels.get(position).getUserName());
        holder.Recycler_Time_message.setText(userChatsModels.get(position).getTime());
        holder.Recycler_lastMessage.setText(userChatsModels.get(position).getLastMessage());

    }

    @Override
    public int getItemCount() {
        return userChatsModels.size();
    }
}
