package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

public class ProfileActivity extends AppCompatActivity {
    private User user;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);
        user = (User) getIntent().getSerializableExtra("user");
        TextView name = findViewById(R.id.tvProfileNameContent);
        TextView email = findViewById(R.id.tvProfileEmailContent);
        TextView gender = findViewById(R.id.tvProfileGenderContent);
        TextView age = findViewById(R.id.tvProfileAgeContent);
        TextView phone = findViewById(R.id.tvProfilePhoneContent);
        TextView userCount = findViewById(R.id.tvUserCount);
        name.setText(user.getName());
        email.setText(user.getEmail());
        gender.setText(user.getGender() + "");
        age.setText(user.getAge() + "");
        phone.setText("0" + user.getPhone());
        dbManager = new DBManager(this);
        dbManager.open();
        int count = dbManager.getUserCount();
        userCount.setText("User #" + user.getId() + " / " + count + " current users");
    }

    public void deleteUser(View view){
        dbManager.deleteUser(user.getId());
        Toast.makeText(this, "User deleted", Toast.LENGTH_LONG).show();
        onBackPressed(); // should redirect to login instead of MainActivity
    }
}
