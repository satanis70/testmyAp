package com.vpiska.UserChatModel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vpiska.R;
import com.vpiska.adModel.ViewHolder;

public class ViewHolderChat extends RecyclerView.ViewHolder{
    TextView Recycyler_nameUser, Recycler_Time_message, Recycler_lastMessage;
    ImageView imageViewChatUser;
    View mView;

    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        Recycyler_nameUser = itemView.findViewById(R.id.Recycyler_name_user);
        imageViewChatUser = itemView.findViewById(R.id.Recycyler_ChatImageUser);
        Recycler_Time_message = itemView.findViewById(R.id.Recycyler_Time_chat);
        Recycler_lastMessage = itemView.findViewById(R.id.Recycyler_Text_chat);

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
