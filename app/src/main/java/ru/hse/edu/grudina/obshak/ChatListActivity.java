package ru.hse.edu.grudina.obshak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChatListActivity extends AppCompatActivity {
    DatabaseReference chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chats = FirebaseDatabase.getInstance().getReference().child("chats");

        displayChatList();
    }

    public void createChat(View view) {
        FirebaseDatabase.getInstance().getReference().child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String chat_title = "chat#" + dataSnapshot.getChildrenCount();

                openChat(Chat.createChat(chat_title).getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    void openChat(String chatId) {
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra("chatId", chatId);

        startActivity(intent);
    }

    private void displayChatList() {
        ListView listOfChats = findViewById(R.id.chatList);

        FirebaseListAdapter<Chat> adapter = new FirebaseListAdapter<Chat>(ChatListActivity.this, Chat.class,
                R.layout.chat, chats) {
            @Override
            protected void populateView(View v, Chat model, int position) {
                TextView chatTitle = v.findViewById(R.id.chat_title);

                chatTitle.setText(model.getTitle());
            }
        };

        listOfChats.setAdapter(adapter);

        listOfChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                chats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for(DataSnapshot t : dataSnapshot.getChildren())
                        {
                            if(i++ < position)
                                continue;
                            openChat(Objects.requireNonNull(Objects.requireNonNull(t.getValue(Chat.class)).getUid()));
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
        });
    }
}
