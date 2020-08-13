package com.vpiska.chat_open_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.vpiska.Chat.ChatMessage;
import com.vpiska.R;
import com.vpiska.UserChatModel.UserChatsModel;

import java.util.List;

public class User_chat_open_adapter extends RecyclerView.Adapter<ViewHolderChatOpen> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final Chat_Open chat_open;
    private Context context;
    List<UserChatsModel> userChatsModelList;
    TextView show_message;
    FirebaseUser firebaseUser;

    public User_chat_open_adapter(Chat_Open chat_open, List<UserChatsModel> userChatsModelList) {
        this.chat_open = chat_open;
        this.userChatsModelList = userChatsModelList;
    }

    @NonNull
    @Override
    public ViewHolderChatOpen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_RIGHT){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolderChatOpen(view);
        } else {
            final View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolderChatOpen(view2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChatOpen holder, int position) {
        UserChatsModel userChatsModel = userChatsModelList.get(position);
        holder.show_message.setText(userChatsModel.getLastMessage());
        Picasso.get().load(userChatsModel.getUserImg()).resize(350,350).into(holder.userImage_open_chat);

    }

    @Override
    public int getItemCount() {
        return userChatsModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (userChatsModelList.get(position).getUserName().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
