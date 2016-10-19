package aakarsh.firebase2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.value;

public class ViewAct extends AppCompatActivity {

    private ListView mainListView ;
    FloatingActionButton button;

    public String userID;
    public String nameOfEntry;
    public FirebaseAuth Auth = FirebaseAuth.getInstance();
    public FirebaseAuth.AuthStateListener AuthListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users = root.child("Users");
    ArrayAdapter<String> listAdapter ;
    ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
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
        mainListView = (ListView) findViewById( R.id.lview);
        button = (FloatingActionButton) findViewById(R.id.button);
        button.setOnClickListener(plusButton);
        getDataOnStart();
    }

    public View.OnClickListener plusButton = new View.OnClickListener() {
        public void onClick (View view){
            createDialog();

        }};

    private void createDialog(){
        LayoutInflater inflater = LayoutInflater.from(ViewAct.this);
        View subView = inflater.inflate(R.layout.dialog_layout, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make an entry?");
        builder.setMessage("Create an entry here");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nameOfEntry = subEditText.getText().toString();
                createUserEntryDB();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    public void createUserEntryDB(){
        createDB(Auth.getCurrentUser().getUid());
    }

    public void createDB(String userID)
    {
        DatabaseReference childRef = users.child(userID);
        childRef.child(nameOfEntry.toString()).setValue("new");
        getData();
    }

    public void getData(){
        DatabaseReference childRef = users.child(user.getUid());

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.add(dataSnapshot.child(nameOfEntry).getKey().toString());
                onPost();
            }
            public void onPost(){
                listAdapter = new ArrayAdapter<String>(ViewAct.this, android.R.layout.simple_list_item_1, arrayList);
                mainListView.setAdapter( listAdapter );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void getDataOnStart(){
        DatabaseReference childRef = users.child(user.getUid());

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot shot: dataSnapshot.getChildren()) {
                    arrayList.add(shot.getKey().toString());
                }
                onPost();
            }
            public void onPost(){
                listAdapter = new ArrayAdapter<String>(ViewAct.this, android.R.layout.simple_list_item_1, arrayList);
                mainListView.setAdapter( listAdapter );
                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), "# " + arrayList.get(position), Toast.LENGTH_SHORT).show();
                        String itemClicked = arrayList.get(position).toString();
                        ListViewClicked l = new ListViewClicked();
                        l.getString(itemClicked);
                      Intent goToClicked = new Intent(getApplicationContext(), ListViewClicked.class);
                        startActivity(goToClicked);
                        //Log.i("", itemClicked);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
