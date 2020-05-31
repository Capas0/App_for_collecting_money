package ru.hse.edu.grudina.obshak;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createChat(View view) {
        Intent intent = new Intent(ChatListActivity.this, ChatCreateActivity.class);
        startActivity(intent);
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
                        for (DataSnapshot t : dataSnapshot.getChildren()) {
                            if (i++ < position)
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
