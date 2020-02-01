package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

public class RegisterActivity extends AppCompatActivity {
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.register);
        dbManager = new DBManager(this);
        dbManager.open();
    }

    public void newUser(View view){
        EditText name = findViewById(R.id.etRegisterName);
        EditText email = findViewById(R.id.etRegisterEmail);
        EditText password = findViewById(R.id.etRegisterPassword);
        EditText confirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        EditText age = findViewById(R.id.etRegisterAge);
        EditText phone = findViewById(R.id.etRegisterPhone);
        RadioGroup gender = findViewById(R.id.rgGender);
        RadioButton male = findViewById(R.id.rbMale);
        RadioButton female = findViewById(R.id.rbFemale);

        if(name.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0 || confirmPassword.getText().length() == 0 ||
                age.getText().length() == 0 || phone.getText().length() == 0 || gender.getCheckedRadioButtonId() == -1)
            Toast.makeText(this, "Please complete all fields!", Toast.LENGTH_SHORT).show();
        else if(phone.getText().length() != 9)
            Toast.makeText(this, "Phone number incomplete! (9 digits)", Toast.LENGTH_SHORT).show();
        else{
            //ar trebui sa verificam daca exista emailul
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                char sex = '?';
                if(male.isChecked())
                    sex = 'M';
                else if(female.isChecked())
                    sex = 'F';
                if(sex == '?'){
                    Toast.makeText(this, "Please select gender!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User newUser = new User(-1, name.getText().toString(), email.getText().toString(), password.getText().toString(),
                        sex, Integer.parseInt(age.getText().toString()), Integer.parseInt(phone.getText().toString()));
                dbManager.insertUser(newUser);
                Toast.makeText(this, "Register successful! Please login.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view){
        onBackPressed();
    }
}
