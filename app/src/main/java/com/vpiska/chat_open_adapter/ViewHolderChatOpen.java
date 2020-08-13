package com.vpiska.chat_open_adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.vpiska.R;

public class ViewHolderChatOpen extends RecyclerView.ViewHolder {
    TextView show_message;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    ImageView userImage_open_chat;
    FirebaseUser firebaseUser;

    public ViewHolderChatOpen(@NonNull View itemView) {
        super(itemView);
        userImage_open_chat = itemView.findViewById(R.id.profile_image);
        show_message = itemView.findViewById(R.id.show_message);
    }
}
