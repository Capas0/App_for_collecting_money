package ru.hse.edu.grudina.obshak;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.edu.grudina.obshak.interfaces.UserCallBack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private User user;

    private Button BTRedact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User.loadUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserCallBack() {
            @Override
            public void onCallback(User value) {
                user = value;
                initialize();
            }
        });
        BTRedact = findViewById(R.id.userProfileRedact);
        BTRedact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileChangeActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                user = (User) data.getSerializableExtra("user");
                if(!User.pushUser(user, FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(ProfileActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                }
                initialize();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_back){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(){
        showMainPart();
        showGeneralPart();
        showContactsPart();
        showSocialPart();
        showAboutMyselfPart();
    }

    private void showMainPart(){
        TextView TVNick = findViewById(R.id.userNickname);
        TextView TVMail = findViewById(R.id.userMail);
        ImageView IMPhoto = findViewById(R.id.imageView);
        if(user.getPhoto() == null){
            IMPhoto.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }else{
            Glide.with(getApplicationContext()).load(user.getPhoto()).into(IMPhoto);
        }
        TVNick.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Marta_Regular.otf"));
        TVMail.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Marta_Regular.otf"));
        TVNick.setText(user.getNickName());
        TVMail.setText(user.getEmail());
    }

    private void showGeneralPart(){
        LinearLayout LLUserGeneral = findViewById(R.id.userGeneral);
        LinearLayout LLUserFullName = findViewById(R.id.userFullNameRow);
        TextView TVUserFullName = findViewById(R.id.userFullName);
        LinearLayout LLUserBirthday = findViewById(R.id.userBirthdayRow);
        TextView TVUserBirthday = findViewById(R.id.userBirthday);
        LinearLayout LLUserCity = findViewById(R.id.userCityRow);
        TextView TVUserCity = findViewById(R.id.userCity);
        LinearLayout LLUserLanguage = findViewById(R.id.userLanguageRow);
        TextView TVUserLanguage = findViewById(R.id.userLanguage);
        LinearLayout LLUserWork = findViewById(R.id.userWorkRow);
        TextView TVUserWork = findViewById(R.id.userWorkName);
        if(setVisible(TVUserFullName, LLUserFullName, user.getFio()) |
                setVisible(TVUserBirthday, LLUserBirthday, user.getBirthday()) |
                setVisible(TVUserCity, LLUserCity, user.getCity()) |
                setVisible(TVUserLanguage, LLUserLanguage, user.getLanguage()) |
                setVisible(TVUserWork, LLUserWork, user.getWork())){
            LLUserGeneral.setVisibility(View.VISIBLE);
        }else{
            LLUserGeneral.setVisibility(View.GONE);
        }
    }

    private void showContactsPart(){
        TextView TVUserEmail = findViewById(R.id.userEmail);
        LinearLayout LLUserPhoneNumber = findViewById(R.id.userPhoneNumberRow);
        TextView TVUserPhoneNumber = findViewById(R.id.userPhoneNumber);
        LinearLayout LLUserAddress = findViewById(R.id.userAdressRow);
        TextView TVUserAddress = findViewById(R.id.userAdress);
        TVUserEmail.setText(user.getEmail());
        setVisible(TVUserPhoneNumber, LLUserPhoneNumber, user.getPhone());
        setVisible(TVUserAddress, LLUserAddress, user.getAddress());
        LinearLayout LLUserContact = findViewById(R.id.userContacts);
        LLUserContact.setVisibility(View.VISIBLE);
    }

    private void showSocialPart(){
        LinearLayout LLUserSocial = findViewById(R.id.userSocial);
        LinearLayout LLUserVK = findViewById(R.id.userVKRow);
        TextView TVUserVK = findViewById(R.id.userVK);
        LinearLayout LLUserInstagram = findViewById(R.id.userInstagramRow);
        TextView TVUserInstagtam = findViewById(R.id.userInstagram);
        LinearLayout LLUserFacebook = findViewById(R.id.userFacebookRow);
        TextView TVUserFacebook = findViewById(R.id.userFacebook);
        LinearLayout LLUserTwitter = findViewById(R.id.userTwitterRow);
        TextView TVUserTwitter = findViewById(R.id.userTwitter);
        LinearLayout LLUserTelegram = findViewById(R.id.userTelegramRow);
        TextView TVUserTelegram = findViewById(R.id.userTelegram);
        LinearLayout LLUserSkype = findViewById(R.id.userSkypeRow);
        TextView TVUserSkype = findViewById(R.id.userSkype);
        if(setVisible(TVUserVK, LLUserVK, user.getVk()) |
                setVisible(TVUserInstagtam, LLUserInstagram, user.getInstagram()) |
                setVisible(TVUserFacebook, LLUserFacebook, user.getFacebook()) |
                setVisible(TVUserTwitter, LLUserTwitter, user.getTwitter()) |
                setVisible(TVUserTelegram, LLUserTelegram, user.getTelegram()) |
                setVisible(TVUserSkype, LLUserSkype, user.getSkype())){
            LLUserSocial.setVisibility(View.VISIBLE);
        }else {
            LLUserSocial.setVisibility(View.GONE);
        }
    }

    private void showAboutMyselfPart(){
        LinearLayout LLUserAboutMyself = findViewById(R.id.userAboutMyself);
        LinearLayout LLUserHobby = findViewById(R.id.userHobbyRow);
        TextView TVUserHobby = findViewById(R.id.userHobby);
        LinearLayout LLUserQuote = findViewById(R.id.userQuoteRow);
        TextView TVUserQuote = findViewById(R.id.userQuote);
        LinearLayout LLUserFilm = findViewById(R.id.userFilmRow);
        TextView TVUserFilm = findViewById(R.id.userFilm);
        LinearLayout LLUserBook = findViewById(R.id.userBookRow);
        TextView TVUserBook = findViewById(R.id.userBook);
        LinearLayout LLUserGroup = findViewById(R.id.userGroupRow);
        TextView TVUserGroup = findViewById(R.id.userGrope);
        LinearLayout LLUserPolitic = findViewById(R.id.userPoliticRow);
        TextView TVUserPolitic = findViewById(R.id.userPolitic);
        if(setVisible(TVUserHobby, LLUserHobby, user.getHobby()) |
                setVisible(TVUserQuote, LLUserQuote, user.getQuote()) |
                setVisible(TVUserFilm, LLUserFilm, user.getFilm()) |
                setVisible(TVUserBook, LLUserBook, user.getBook()) |
                setVisible(TVUserGroup, LLUserGroup, user.getMusicalGroup()) |
                setVisible(TVUserPolitic, LLUserPolitic, user.getPoliticalPreferences())){
            LLUserAboutMyself.setVisibility(View.VISIBLE);
        }else{
            LLUserAboutMyself.setVisibility(View.GONE);
        }
    }

    private boolean setVisible(TextView view, LinearLayout space, String value){
        if(value == null || value.length() == 0){
            space.setVisibility(View.GONE);
            return false;
        }else{
            space.setVisibility(View.VISIBLE);
            view.setText(value);
            return true;
        }
    }
}
