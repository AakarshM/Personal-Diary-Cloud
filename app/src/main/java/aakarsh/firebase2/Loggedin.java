package aakarsh.firebase2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loggedin extends AppCompatActivity {

    Button changeMail, changePass, disp, sgnout, del, databaseBut;
    String dispName;
    String userEmail;
    String userID;
    TextView textView;
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    public FirebaseAuth.AuthStateListener AuthListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    //DatabaseReference newUser = users.child("");


    public View.OnClickListener changePassListener = new View.OnClickListener() {
        public void onClick (View view){
            Auth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Loggedin.this, "Email sent", Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(Loggedin.this, "Could not send an E-Mail", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


        }};

    public View.OnClickListener changeMailListener = new View.OnClickListener() {
        public void onClick (View view){
            Intent changeMailAct = new Intent(getApplicationContext(), mailChangActivity.class);
            startActivity(changeMailAct);

        }};

    public View.OnClickListener delListener = new View.OnClickListener() {
        public void onClick (View view){
            delAcc();

        }};

    public View.OnClickListener sgnOutListener = new View.OnClickListener() {
        public void onClick (View view){
            sgnOutNow();
            finish();
            Intent FrontActivity = new Intent(getApplicationContext(), FrontPageActivity.class);
            startActivity(FrontActivity);



        }};

        public View.OnClickListener dispListener = new View.OnClickListener() {
            public void onClick (View view){
                Intent changeDispAct = new Intent(getApplicationContext(), changeDispActivity.class);
                startActivity(changeDispAct);


            }};


    public View.OnClickListener databaseButListener = new View.OnClickListener() {
        public void onClick (View view){
          //  Intent databaseAct = new Intent(getApplicationContext(), database.class);
            Intent databaseAct = new Intent(getApplicationContext(), ViewAct.class);
            startActivity(databaseAct);
        }};


    public void delAcc(){

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Sorry to see you go", Toast.LENGTH_LONG).show();
                            Intent frontAct = new Intent(getApplicationContext(), FrontPageActivity.class);
                            startActivity(frontAct);
                        }
                    }
                });


    }

    public void sgnOutNow(){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent FrontActivity = new Intent(getApplicationContext(), FrontPageActivity.class);
        startActivity(FrontActivity);

    }

    public void createDB(){


        users.child(userID).setValue("");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        changeMail = (Button) findViewById(R.id.changeMail);
        changePass = (Button) findViewById(R.id.changePass);
        databaseBut = (Button) findViewById(R.id.databaseButton);
        disp = (Button)findViewById(R.id.disp);
        textView = (TextView) findViewById(R.id.textView4);
        sgnout = (Button)findViewById(R.id.Sgnout);
        del = (Button)findViewById(R.id.del);
        disp.setOnClickListener(dispListener);
        sgnout.setOnClickListener(sgnOutListener);
        del.setOnClickListener(delListener);
        changePass.setOnClickListener(changePassListener);
        changeMail.setOnClickListener(changeMailListener);
        databaseBut.setOnClickListener(databaseButListener);
        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();
                    Log.i("Hello,", user.getEmail());
                    dispName = user.getDisplayName();
                    userEmail = user.getEmail();
                   // createDB();
                    if(dispName != null){
                        textView.setText("What would you like to do today " + dispName + " ?");
                        Toast.makeText(Loggedin.this, "Hello " + dispName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loggedin.this, "Hello", Toast.LENGTH_SHORT).show();
                        textView.setText("What would you like to do today?");
                    }

                } else {
                    Log.i("Problem:", "couldn't login.");
                }
            }
        };FirebaseAuth.getInstance().addAuthStateListener(AuthListener);

    }

}
