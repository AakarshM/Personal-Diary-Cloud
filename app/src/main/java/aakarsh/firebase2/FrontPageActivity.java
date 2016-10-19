package aakarsh.firebase2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.button;

public class FrontPageActivity extends AppCompatActivity {

    Button button1; //log in
    Button button2; //sign up

    public View.OnClickListener button1Listener = new View.OnClickListener() {
        public void onClick (View view){

            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);


        }};

    public View.OnClickListener button2Listener = new View.OnClickListener() {
        public void onClick (View view){
            Intent signupActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(signupActivity);


        }};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        button1 = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(button1Listener);
        button2.setOnClickListener(button2Listener);
    }
}
