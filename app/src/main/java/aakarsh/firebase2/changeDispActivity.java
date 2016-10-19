package aakarsh.firebase2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class changeDispActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public FirebaseAuth Auth;
    public FirebaseAuth.AuthStateListener AuthListener;
    EditText dispname;
    Button button;
    String newDispName = null;
    ProgressBar bar;
    Button backbut;

    public View.OnClickListener changeDispListener = new View.OnClickListener() {
        public void onClick(View view) {
            changeDisp();

        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent backToLoggedIn = new Intent(getApplicationContext(), Loggedin.class);
            startActivity(backToLoggedIn);
            finish();
        }
    };

    public void changeDisp(){
        newDispName = dispname.getText().toString();
        bar.setVisibility(View.VISIBLE);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newDispName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);
                            Intent backToLoggedIn = new Intent(getApplicationContext(), Loggedin.class);
                            startActivity(backToLoggedIn);
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_disp);
        dispname = (EditText) findViewById(R.id.dispField);
        button = (Button) findViewById(R.id.dispButton);
        bar = (ProgressBar) findViewById(R.id.bar);
        button.setOnClickListener(changeDispListener);
        bar.setVisibility(View.INVISIBLE);
        backbut = (Button)findViewById(R.id.backBut);
        backbut.setOnClickListener(backListener);
        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("Hello,", user.getEmail());
                } else {
                    Log.i("Problem:", "couldn't login.");
                }

            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(AuthListener);


    }


    }



