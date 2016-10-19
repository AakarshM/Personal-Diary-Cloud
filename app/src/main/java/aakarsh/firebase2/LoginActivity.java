package aakarsh.firebase2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.password;

public class LoginActivity extends AppCompatActivity {

    EditText editText;
    EditText editText2;
    Button button, backbutton;
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    public FirebaseAuth.AuthStateListener authlistener;
    String email, pass;
    ProgressBar bar;

    public View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick (View view){
            email = editText.getText().toString().trim();
            pass = editText2.getText().toString().trim();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            logInUser();

        }};

    public View.OnClickListener backButListener = new View.OnClickListener() {
        public void onClick (View view){
            Intent frontPage = new Intent(getApplicationContext(), FrontPageActivity.class);
            startActivity(frontPage);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        button = (Button)findViewById(R.id.button2);
        Auth = FirebaseAuth.getInstance();
        button.setOnClickListener(buttonListener);
        bar = (ProgressBar)findViewById(R.id.progressBar2);
        bar.setVisibility(View.INVISIBLE);
        backbutton = (Button)findViewById(R.id.backBut);
        backbutton.setOnClickListener(backButListener);

        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }
                // ...
            }
        };
        // ...
    }

    @Override
    public void onStart() {
        super.onStart();
        Auth.addAuthStateListener(authlistener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authlistener != null) {
            Auth.removeAuthStateListener(authlistener);
        }
    }

    public void logInUser(){
        bar.setVisibility(View.VISIBLE);
        Auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent loggedInActivity = new Intent(getApplicationContext(), Loggedin.class);
                            startActivity(loggedInActivity);
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "There was an error, try again", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}
