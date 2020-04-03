package ru.hse.edu.grudina.obshak;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView TVNick = findViewById(R.id.userNickname);
        TVNick.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/Marta_Regular.otf"));
        TextView TVMail = findViewById(R.id.userMail);
        TVMail.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/Marta_Regular.otf"));
    }
}
