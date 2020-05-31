package ru.hse.edu.grudina.obshak;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class Chat {
    private String title;
    private String uid;

    public Chat() {
    }

    static Chat createChat(String title) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chats").push();

        Chat instance = new Chat();
        instance.title = title;
        instance.uid = ref.getKey();

        ref.setValue(instance);
        return instance;
    }

    static Chat loadChat(String uid) {
        final Chat instance = new Chat();
        instance.uid = uid;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chats").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                instance.title = Objects.requireNonNull(dataSnapshot.getValue(Chat.class)).title;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        return instance;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
