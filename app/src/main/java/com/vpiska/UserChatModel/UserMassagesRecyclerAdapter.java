package com.vpiska.UserChatModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.vpiska.Chat.UserMessagesActivity;
import com.vpiska.R;
import com.vpiska.adModel.ViewHolder;
import com.vpiska.chat_open_adapter.Chat_Open;

import java.util.List;

public class UserMassagesRecyclerAdapter extends RecyclerView.Adapter<ViewHolderChat>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    TextView show_message;
    Context context;
    FirebaseUser firebaseUser;
    UserMessagesActivity userMessagesActivity;
    List<UserChatsModel> userChatsModels;
    ImageView imageView, profile_image;
    String autor;
    public UserMassagesRecyclerAdapter(UserMessagesActivity userMessagesActivity, List<UserChatsModel> userChatsModels) {
        this.userMessagesActivity = userMessagesActivity;
        this.userChatsModels = userChatsModels;
    }
    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_layout_item, parent, false);
        final View chat_item_left_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);

        ViewHolderChat viewHolder = new ViewHolderChat(itemView);
        imageView = itemView.findViewById(R.id.Recycyler_ChatImageUser);
        profile_image = itemView.findViewById(R.id.profile_image);
        show_message = itemView.findViewById(R.id.show_message);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                autor = userChatsModels.get(position).getIdUser();
                Toast.makeText(userMessagesActivity, autor, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Chat_Open.class);
                intent.putExtra("User", autor);
                view.getContext().startActivity(intent);
                context = view.getContext();
                ((Activity)context).finish();


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
