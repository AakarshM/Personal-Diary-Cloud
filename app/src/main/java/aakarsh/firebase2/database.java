package aakarsh.firebase2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class database extends AppCompatActivity {

    EditText writeText;
    TextView showText;
    Button goButton;
    public String userID;
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    public FirebaseAuth.AuthStateListener AuthListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");





    public View.OnClickListener goButListener = new View.OnClickListener() {
        public void onClick (View view){
                writeTextMsg();
                retrive();
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        writeText = (EditText)findViewById(R.id.writeText);
        showText = (TextView)findViewById(R.id.showText);
        goButton = (Button)findViewById(R.id.goButton);
        goButton.setOnClickListener(goButListener);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                     userID = user.getUid().toString();
                } else {
                    Log.i("Problem:", "couldn't login.");
                }
            }
        };FirebaseAuth.getInstance().addAuthStateListener(AuthListener);
    }



    public void writeTextMsg(){
        Map<String, Object> update = new HashMap<>();
        update.put(userID, writeText.getText().toString());
        users.updateChildren(update);
    }

    public void retrive(){

        users.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valueFromDB = dataSnapshot.getValue(String.class);
                showText.setText(valueFromDB.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
