package ru.hse.edu.grudina.obshak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatCreateActivity extends AppCompatActivity {
    EditText groupTitle;
    EditText addUserNickname;
    Button addUserButton;
    ListView createGroupUsersListView;
    DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference nicknames = FirebaseDatabase.getInstance().getReference().child("nickNames");
    final ArrayList<String> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_create);
        groupTitle = findViewById(R.id.groupTitle);
        addUserNickname = findViewById(R.id.addUserNickname);
        addUserButton = findViewById(R.id.addUserButton);
        createGroupUsersListView = findViewById(R.id.createGroupUsersListView);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.add(dataSnapshot.child(Objects.requireNonNull(
                        FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .child("nickName")
                        .getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ChatCreateActivity.this,
                        android.R.layout.simple_list_item_1, usersList);
        createGroupUsersListView.setAdapter(adapter);
    }

    public void createChat(View view) {
        final DatabaseReference knownChats = FirebaseDatabase.getInstance().getReference().child("knownChats");
        final String chatUid = Chat.createChat(Objects.requireNonNull(groupTitle.getText()).toString()).getUid();

        nicknames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < usersList.size(); i++) {
                    String uid = dataSnapshot.child(usersList.get(i)).getValue(String.class);
                    knownChats.child(Objects.requireNonNull(uid)).push().setValue(chatUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                openChat(chatUid);
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

    public void addUser(View view) {
        final String nickname = addUserNickname.getText().toString();
        User.isNicknameExists(nickname, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean) {
                    usersList.add(nickname);
                    addUserNickname.getText().clear();
                } else {
                    Toast.makeText(ChatCreateActivity.this, "Пользователя с таким никнеймом не существует", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
