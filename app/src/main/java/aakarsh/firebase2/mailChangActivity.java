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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mailChangActivity extends AppCompatActivity {
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    public FirebaseAuth.AuthStateListener authlistener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String newEmail;
    Button button, backButton;
    EditText text;
    ProgressBar bar;

    public View.OnClickListener changeMailListener = new View.OnClickListener() {
        public void onClick (View view){
           newEmail = text.getText().toString();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            changeMail();
        }};

    public View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick (View view){
            Intent frontPage = new Intent(getApplicationContext(), FrontPageActivity.class);
            startActivity(frontPage);
        }};


    public void changeMail(){
        bar.setVisibility(View.VISIBLE);
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_chang);
        button = (Button)findViewById(R.id.button6);
        bar = (ProgressBar)findViewById(R.id.progressBar3);
        bar.setVisibility(View.INVISIBLE);
        text = (EditText)findViewById(R.id.editText3);
        button.setOnClickListener(changeMailListener);
        backButton = (Button)findViewById(R.id.backBut);
        backButton.setOnClickListener(backListener);
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("Checked:", "User is signed in");
                } else {
                    Log.i("Checked:", "User is signed out");
                }
            }
        }; Auth.addAuthStateListener(authlistener);
    }
}
