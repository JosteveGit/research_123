package com.example.research;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    Button signIn;
    Button register;
    ArrayList<String> emailss = new ArrayList<>();
    ArrayList<String> passwordsss =new ArrayList<>();


    android.support.v7.widget.AppCompatEditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        signIn = findViewById(R.id.signIn);
        register = findViewById(R.id.register);
        email = findViewById(R.id.emails);
        password = findViewById(R.id.passwords);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor myusers = databaseHelper.getUsers();
                while (myusers.moveToNext()){
                    emailss.add(myusers.getString(1));
                    passwordsss.add(myusers.getString(2));
                }
                boolean exist = false;
                for (int i=0;i<emailss.size();i++){
                    if(emailss.get(i).equals(email.getText().toString())){
                        exist=true;
                        if(passwordsss.get(i).equals(password.getText().toString())){
                            startActivity(new Intent(MainActivity.this,SearchActivity.class));
                            break;
                        }else{
                            Toast.makeText(getApplicationContext(),"Password is incorrect",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if(!exist){
                    Toast.makeText(getApplicationContext(),"User not found",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
    }
}
