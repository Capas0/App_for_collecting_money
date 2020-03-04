package ru.hse.edu.grudina.obshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText Email;
    private EditText Password;
    private TextView Question;
    private TextView LogIn;
    private Button Login;
    private Button Register;

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
        Question = findViewById(R.id.questionTV);
        LogIn = findViewById(R.id.returnTV);
        Register = findViewById(R.id.registerBT);

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
                signIn(Email.getText().toString(), Password.getText().toString());
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
                registrationUser(Email.getText().toString(), Password.getText().toString());
            }
        });
        Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question.setVisibility(View.INVISIBLE);
                LogIn.setVisibility(View.VISIBLE);
                Login.setVisibility(View.INVISIBLE);
                Register.setVisibility(View.VISIBLE);
            }
        });
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question.setVisibility(View.VISIBLE);
                LogIn.setVisibility(View.INVISIBLE);
                Login.setVisibility(View.VISIBLE);
                Register.setVisibility(View.INVISIBLE);
                Password.setText("");
            }
        });
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                    Toast.makeText(MainActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrationUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                    Toast.makeText(MainActivity.this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
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
}