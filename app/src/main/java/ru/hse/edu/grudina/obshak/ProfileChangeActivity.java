package ru.hse.edu.grudina.obshak;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class ProfileChangeActivity extends AppCompatActivity {

    private TextView Type;
    private ImageView Photo;
    private EditText Nick, FullName, Birthday, City,
            Language, Work, Email, Phone, Address, VK,
            Instagram, Facebook, Twitter, Telegram,
            Skype, Hobby, Quote, Film, Book, Group;
    private Spinner Politic;
    private Button Cansel, Reset, Apply;

    private User user;
    private String photoUri;
    private boolean isCorrect;
    private Calendar today;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);
        getSupportActionBar().hide();
        user = (User) getIntent().getSerializableExtra("user");
        initializeTab();
        initializeElements();
        setValueElements();

        Cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUri.length() > 0) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUri);
                    storageReference.delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
                setResult(RESULT_CANCELED);
                isCorrect = true;
                finish();
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUri.length() > 0) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUri);
                    storageReference.delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
                setValueElements();
            }
        });

        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueUser(new Runnable() {
                    @Override
                    public void run() {
                        Intent result = new Intent();
                        if (photoUri.length() > 0) {
                            if (user.getPhoto() != null && user.getPhoto().length() > 0) {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getPhoto());
                                storageReference.delete().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            }
                            user.setPhoto(photoUri);
                        }
                        result.putExtra("user", user);
                        setResult(RESULT_OK, result);
                        isCorrect = true;
                        finish();
                    }
                });
            }
        });

        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Birthday.getText() != null || Birthday.getText().toString().length() > 0) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        today.setTime(sdf.parse(Birthday.getText().toString()));
                    } catch (Throwable ex) {

                    }
                }
                new DatePickerDialog(ProfileChangeActivity.this, d,
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (!isCorrect) {
            if (photoUri.length() > 0) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUri);
                storageReference.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }
        super.onDestroy();
    }

    private void initializeTab() {
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("general");

        tabSpec.setContent(R.id.tab_general);
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.tab_general));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("contacts");
        tabSpec.setContent(R.id.tab_contacts);
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.tab_contactes));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("social");
        tabSpec.setContent(R.id.tab_social);
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.tab_social));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("aboutMyself");
        tabSpec.setContent(R.id.tab_about_myself);
        tabSpec.setIndicator("", getResources().getDrawable(R.drawable.tab_about_myself));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "contacts":
                        Type.setText(R.string.user_contacts_text);
                        break;
                    case "social":
                        Type.setText(R.string.user_social_text);
                        break;
                    case "general":
                        Type.setText(R.string.user_general_text);
                        break;
                    default:
                        Type.setText(R.string.user_aboutmyself_text);
                        break;
                }
            }
        });
    }

    private void initializeElements() {
        Photo = findViewById(R.id.user_photo_change);
        Type = findViewById(R.id.tab_type);
        Nick = findViewById(R.id.userNickChange);
        FullName = findViewById(R.id.userFullNameChange);
        Birthday = findViewById(R.id.userBirthdayChange);
        City = findViewById(R.id.userCityChange);
        Language = findViewById(R.id.userLanguageChange);
        Work = findViewById(R.id.userWorkChange);
        Email = findViewById(R.id.userEmailChange);
        Phone = findViewById(R.id.userPhoneNumberChange);
        Address = findViewById(R.id.userAddressChange);
        Instagram = findViewById(R.id.userInstagramChange);
        VK = findViewById(R.id.userVKChange);
        Facebook = findViewById(R.id.userFacebookChange);
        Twitter = findViewById(R.id.userTwitterChange);
        Telegram = findViewById(R.id.userTelegramChange);
        Skype = findViewById(R.id.userSkypeChange);
        Hobby = findViewById(R.id.userHobbyChange);
        Quote = findViewById(R.id.userQuoteChange);
        Film = findViewById(R.id.userFilmChange);
        Book = findViewById(R.id.userBookChange);
        Group = findViewById(R.id.userGropeChange);
        Politic = findViewById(R.id.userPoliticChange);
        Cansel = findViewById(R.id.profile_change_cancel);
        Reset = findViewById(R.id.profile_change_reset);
        Apply = findViewById(R.id.profile_change_apply);
    }

    private void setValueElements() {
        if (user.getPhoto() == null) {
            Photo.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        } else {
            Glide.with(getApplicationContext()).load(user.getPhoto()).into(Photo);
        }
        photoUri = "";
        isCorrect = false;
        today = Calendar.getInstance();
        Nick.setText(user.getNickName());
        FullName.setText(user.getFio());
        Birthday.setText(user.getBirthday());
        Birthday.setFocusable(false);
        City.setText(user.getCity());
        Language.setText(user.getLanguage());
        Work.setText(user.getWork());
        Email.setText(user.getEmail());
        Phone.setText(user.getPhone());
        Address.setText(user.getAddress());
        VK.setText(user.getVk());
        Instagram.setText(user.getInstagram());
        Facebook.setText(user.getFacebook());
        Twitter.setText(user.getTwitter());
        Telegram.setText(user.getTelegram());
        Skype.setText(user.getSkype());
        Hobby.setText(user.getHobby());
        Quote.setText(user.getQuote());
        Film.setText(user.getFilm());
        Book.setText(user.getBook());
        Group.setText(user.getMusicalGroup());
        String[] mas = getResources().getStringArray(R.array.politics);
        int i;
        for (i = 0; i < mas.length; i++) {
            if (mas[i].equals(user.getPoliticalPreferences() == null ? "" : user.getPoliticalPreferences())) {
                break;
            }
        }
        Politic.setSelection(i);
    }

    private void setValueUser(final Runnable onFinish) {
        user.setFio(FullName.getText().toString());
        user.setBirthday(Birthday.getText().toString());
        user.setCity(City.getText().toString());
        user.setLanguage(Language.getText().toString());
        user.setWork(Work.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setPhone(Phone.getText().toString());
        user.setAddress(Address.getText().toString());
        user.setVk(VK.getText().toString());
        user.setInstagram(Instagram.getText().toString());
        user.setFacebook(Facebook.getText().toString());
        user.setTwitter(Twitter.getText().toString());
        user.setTelegram(Telegram.getText().toString());
        user.setSkype(Skype.getText().toString());
        user.setHobby(Hobby.getText().toString());
        user.setQuote(Quote.getText().toString());
        user.setFilm(Film.getText().toString());
        user.setBook(Book.getText().toString());
        user.setMusicalGroup(Group.getText().toString());
        user.setPoliticalPreferences(Politic.getSelectedItem().toString());

        final String nick = Nick.getText().toString();
        User.isNicknameExists(nick, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (!aBoolean) {
                    user.setNickName(nick);
                } else {
                    Toast.makeText(ProfileChangeActivity.this, "Никнейм занят", Toast.LENGTH_SHORT).show();
                }
                onFinish.run();
            }
        });
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            today.set(Calendar.YEAR, year);
            today.set(Calendar.MONTH, monthOfYear);
            today.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        Birthday.setText(df.format(today.getTime()));
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Photo.setImageBitmap(bitmap);
                uploadImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final String uuid = UUID.randomUUID().toString();
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + uuid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileChangeActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (photoUri.length() > 0) {
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUri);
                                        storageReference.delete().addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                                    }
                                    photoUri = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileChangeActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
