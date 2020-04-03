package ru.hse.edu.grudina.obshak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText Email;
    private EditText Password;
    private EditText NickName;
    private TextView Question;
    private TextView LogIn;
    private TextView ResetPassword;
    private Button Login;
    private Button Register;
    private Button Reset;
    private ImageView Image;
    private LinearLayout Layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };

        Email = findViewById(R.id.logIn);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.logInBT);
        NickName = findViewById(R.id.nickName);
        ResetPassword = findViewById(R.id.resetPasswordTV);
        Question = findViewById(R.id.questionTV);
        LogIn = findViewById(R.id.returnTV);
        Register = findViewById(R.id.registerBT);
        Reset = findViewById(R.id.resetPasswordBT);
        Image = findViewById(R.id.imageView);
        Layout = findViewById(R.id.linearLayout);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUserNameValid(Email.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.email_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPasswordValid(Password.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.password_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                signIn(Email.getText().toString(), Password.getText().toString(), "");
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUserNameValid(Email.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.email_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isPasswordValid(Password.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.password_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isNickNameValid(NickName.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.nick_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                registrationUser(Email.getText().toString(), Password.getText().toString());
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUserNameValid(Email.getText().toString())){
                    Toast.makeText(MainActivity.this, R.string.email_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                resetPassword(Email.getText().toString());
            }
        });
        Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegisterView();
            }
        });
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginView();
            }
        });
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResetPasswordView();
            }
        });
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences save = getSharedPreferences("SAVE", 0);
        Email.setText(save.getString("email", ""));


        if(mAuth.getCurrentUser() != null){
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(getString(R.string.quick_enter) + " " + mAuth.getCurrentUser().getDisplayName() + "?");
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.yes, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder builder = new StringBuilder();
                            builder.append(" ").append(getString(R.string.login_success)).append(", ").append(mAuth.getCurrentUser().getDisplayName()).append('!');
                            Toast.makeText(MainActivity.this, builder, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                            startActivity(intent);
                        }
                    });
            dialog.setNegativeButton(R.string.no, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_program){
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(R.string.about_program_info);
            dialog.setCancelable(false);
            dialog.setNegativeButton(R.string.ok, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }else if(id == R.id.close){
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(R.string.close_question);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.yes, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });
            dialog.setNegativeButton(R.string.no, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }else if(id == R.id.closeAndExit){
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(R.string.close_question);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.yes, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            MainActivity.this.finish();
                        }
                    });
            dialog.setNegativeButton(R.string.no, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void signIn(String email, String password, final String start) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SharedPreferences save = getSharedPreferences("SAVE",0);
                    SharedPreferences.Editor editor = save.edit();
                    editor.putString("email", Email.getText().toString());
                    editor.commit();

                    StringBuilder builder = new StringBuilder();
                    builder.append(start).append(" ").append(getString(R.string.login_success)).append(", ").append(mAuth.getCurrentUser().getDisplayName()).append('!');
                    Toast.makeText(MainActivity.this, builder, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrationUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                            .setDisplayName(NickName.getText().toString())
                            .build();
                    mAuth.getCurrentUser().updateProfile(profileChange);
                    signIn(email, password, getString(R.string.registration_success));
                } else {
                    Toast.makeText(MainActivity.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPassword(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, R.string.reset_success, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, R.string.reset_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 5;
    }

    private boolean isNickNameValid(String nickName) {
        return nickName != null && nickName.length() > 2;
    }

    private void setLoginView(){
        Layout.setBackground(getDrawable(R.drawable.green_frame_two));
        Email.setVisibility(View.VISIBLE);
        Password.setVisibility(View.VISIBLE);
        NickName.setVisibility(View.GONE);
        Question.setVisibility(View.VISIBLE);
        LogIn.setVisibility(View.INVISIBLE);
        ResetPassword.setVisibility(View.VISIBLE);
        Login.setVisibility(View.VISIBLE);
        Register.setVisibility(View.INVISIBLE);
        Reset.setVisibility(View.INVISIBLE);
        Password.setText("");
    }

    private void setRegisterView(){
        Layout.setBackground(getDrawable(R.drawable.red_frame_two));
        Email.setVisibility(View.VISIBLE);
        Password.setVisibility(View.VISIBLE);
        NickName.setVisibility(View.VISIBLE);
        Question.setVisibility(View.INVISIBLE);
        LogIn.setVisibility(View.VISIBLE);
        ResetPassword.setVisibility(View.INVISIBLE);
        Login.setVisibility(View.INVISIBLE);
        Register.setVisibility(View.VISIBLE);
        Reset.setVisibility(View.INVISIBLE);
        Password.setText("");
        NickName.setText("");
    }

    private void setResetPasswordView(){
        Layout.setBackground(getDrawable(R.drawable.red_frame_two));
        Email.setVisibility(View.VISIBLE);
        Password.setVisibility(View.GONE);
        NickName.setVisibility(View.GONE);
        Question.setVisibility(View.INVISIBLE);
        LogIn.setVisibility(View.VISIBLE);
        ResetPassword.setVisibility(View.INVISIBLE);
        Login.setVisibility(View.INVISIBLE);
        Register.setVisibility(View.INVISIBLE);
        Reset.setVisibility(View.VISIBLE);
    }
}