package ru.hse.edu.grudina.obshak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChatCreateActivity extends AppCompatActivity {
    EditText groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_create);
        groupTitle = findViewById(R.id.groupTitle);
    }

    public void createChat(View view) {
        FirebaseDatabase.getInstance().getReference().child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                openChat(Chat.createChat(Objects.requireNonNull(groupTitle.getText()).toString()).getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    void openChat(String chatId) {
        Intent intent = new Intent(ChatCreateActivity.this, ChatActivity.class);
        intent.putExtra("chatId", chatId);

        startActivity(intent);
    }
}
