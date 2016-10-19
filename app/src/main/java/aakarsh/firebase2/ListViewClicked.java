package aakarsh.firebase2;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewClicked extends AppCompatActivity {

    EditText editText;
    public String userID;
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    FloatingActionButton actionButton;
    public FirebaseAuth.AuthStateListener AuthListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    //String itemClickedName = getIntent().getExtras().getString("key");
    public static String new1;
    public void getString (String s){
        new1 = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_clicked);
        editText = (EditText)findViewById(R.id.editText4);
        editText.setImeActionLabel("Enter", KeyEvent.KEYCODE_ENTER);
        actionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(saveButton);
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
        getDataOnCreate();
    }

    public View.OnClickListener saveButton = new View.OnClickListener() {
        public void onClick (View view){
            saveData();

        }};

    public void test(){
            Log.i("", new1);

    }

    String dataFound;
    public void getDataOnCreate(){
        DatabaseReference childRef = users.child(user.getUid());
        DatabaseReference keyRef = childRef.child(new1);
        keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataFound = dataSnapshot.getValue().toString();
                onPost();
            }
            public void onPost(){
                editText.setText(dataFound);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void saveData(){
        DatabaseReference childRef = users.child(user.getUid());
        DatabaseReference keyRef = childRef.child(new1);
        Map<String, Object> update = new HashMap<>();
        update.put(keyRef.getKey(), editText.getText().toString());
        childRef.updateChildren(update);
        Toast.makeText(ListViewClicked.this, "Save successful!", Toast.LENGTH_LONG).show();
    }
}
