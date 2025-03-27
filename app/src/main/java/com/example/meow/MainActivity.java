package com.example.meow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meow.adapter.ChatAdapter;
import com.example.meow.model.Message;
import com.example.meow.onetimeauth.PrefManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText messageInput;
    private Button sendButton;
    private List<Message> messageList;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");

        PrefManager prefManager = new PrefManager(this);
        checkIfFirstTime(prefManager);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // If permission is not granted, request it
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
//            }
//        }

        recyclerView = findViewById(R.id.recyclerViewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messageList);
        recyclerView.setAdapter(chatAdapter);
        sendButton.setOnClickListener(v -> sendMessage(prefManager));
        listenForMessages();
    }

    private void sendMessage(PrefManager prefManager) {
        String text = messageInput.getText().toString().trim();
        if (!text.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            String msgSender = prefManager.getUserType();
            System.out.println(prefManager.getUserType()+" setting in entity");
            Message message = new Message(text, msgSender, timestamp);
            System.out.println(message.getSender()+" got from message in entity");
            databaseReference.push().setValue(message);

            // Clear input field
            messageInput.setText("");
        }
    }

    private void listenForMessages() {
        databaseReference.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);

                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

    private boolean checkIfFirstTime(PrefManager prefManager){
        if (prefManager.isFirstTime()) {
            // Navigate to the PIN verification screen
            startActivity(new Intent(this, com.example.meow.onetimeauth.PinVerifyActivity.class));
            finish();
            return true;
        } else {
            // Load the main activity layout
            setContentView(R.layout.activity_main);
            return false;
        }
    }


}