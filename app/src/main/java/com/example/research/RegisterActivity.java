package com.example.research;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    android.support.v7.widget.AppCompatEditText email,password,confirmPassword,otherSecOPt1,otherSecOPt2;


   static DatabaseHelper databaseHelper;

    public boolean verifyEmail(String password){
        String at = "@";
        String dotCom = ".com";

        if(password.contains(at) && password.contains(dotCom)){
            return true;
        }else{
            return false;
        }
    }
    public boolean nonEqualsNull(String... details){
        boolean nonEqualsNull = true;
        for(String values : details){
            if(values.trim().equals("")){
                nonEqualsNull=false;
                break;
            }
        }
        return nonEqualsNull;
    }
    public boolean passwordsMatch(String password,String confirmPassword){
        if(password.equals(confirmPassword)){
            return true;
        }else{
            return false;
        }
    }
    public boolean passwordMeetsRequirement(String password){

        if(password.length()>7){
            return true;
        }else{
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        otherSecOPt1 = findViewById(R.id.firstQuestion);
        otherSecOPt2 = findViewById(R.id.secondQuestion);


        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ems = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String cpass = confirmPassword.getText().toString().trim();
                String opt1 = otherSecOPt1.getText().toString().trim();
                String opt2 = otherSecOPt2.getText().toString().trim();

                if(nonEqualsNull(ems,pass,cpass,opt1,opt2)){
                    if(verifyEmail(ems)){
                        if(passwordMeetsRequirement(pass)){
                            if(passwordsMatch(pass,cpass)){
                                //Upload to db
                                //Start Activity

                                databaseHelper.addUsers(ems,pass,opt1,opt2);
                                startActivity(new Intent(RegisterActivity.this,SelectFields.class));
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(),"Passwords didn't match",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Password lenght must be at least of 8 characters",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Email is invalid",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
