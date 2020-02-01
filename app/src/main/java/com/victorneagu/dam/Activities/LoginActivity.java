package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText email, password;
    CheckBox remember;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getApplicationContext().getSharedPreferences("login", 0);
        editor = pref.edit();
        email = findViewById(R.id.etLoginEmail);
        password = findViewById(R.id.etLoginPassword);
        remember = findViewById(R.id.cbRememberMe);
        String savedEmail = pref.getString("email", null);
        String savedPassword = pref.getString("password", null);
        if(savedEmail != null && savedPassword != null){
            email.setText(savedEmail);
            password.setText(savedPassword);
            remember.setChecked(true);
        }
        dbManager = new DBManager(this);
        dbManager.open();
    }

    public void login(View view){
        if(email.length() > 0 && password.length() > 0){
            if(remember.isChecked()){
                editor.putString("email", email.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
            }
            else{
                editor.remove("email");
                editor.remove("password");
                editor.commit();
            }
            User user = dbManager.loginUser(email.getText().toString(), password.getText().toString());
            if(user != null){
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                loginIntent.putExtra("user", user);
                LoginActivity.this.startActivity(loginIntent);
            }
            else
                Toast.makeText(this, "Incorrect credentials OR user doesn't exist!", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Complete credentials!", Toast.LENGTH_SHORT).show();
    }

    public void register(View view){
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    public void forgotPassword(View view){
        if(email.length() == 0)
            Toast.makeText(this, "Please complete your email", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Your password is: " + dbManager.forgotPasswordUser(email.getText().toString()), Toast.LENGTH_LONG).show();
    }
}
