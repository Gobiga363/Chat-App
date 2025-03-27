package com.example.meow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meow.R;
import com.example.meow.model.Message;
import com.example.meow.onetimeauth.PrefManager;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> messageList;
    private Context context;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.textViewMessage.setText(message.getText());

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.textViewMessage.getLayoutParams();
        PrefManager prefManager = new PrefManager(context);

        if (prefManager.getUserType().equals(message.getSender())) {
            holder.textViewMessage.setBackgroundResource(R.drawable.message_sent);
            holder.textViewMessage.setTextColor(context.getResources().getColor(android.R.color.white));
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;  // Align to right
            params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        } else {
            holder.textViewMessage.setBackgroundResource(R.drawable.message_received);
            holder.textViewMessage.setTextColor(context.getResources().getColor(android.R.color.black));
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // Align to left
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        }

        holder.textViewMessage.setLayoutParams(params);
    }

    public void updateMessages(List<Message> messages) {
        this.messageList = messages;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            System.out.println("text view msg------------"+textViewMessage);
        }
    }
}
