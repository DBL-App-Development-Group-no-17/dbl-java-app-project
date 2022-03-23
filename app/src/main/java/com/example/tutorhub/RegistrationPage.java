package com.example.tutorhub;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        // variables
        EditText userName = findViewById(R.id.editTextTextPersonName3);
        EditText name = findViewById(R.id.editTextTextPersonName);
        EditText email = findViewById(R.id.editTextTextPersonName2);
        EditText educationalInstitution = findViewById(R.id.editTextTextPersonName4);
        EditText phoneNumber = findViewById(R.id.editTextTextPersonName5);
        EditText password = findViewById(R.id.editTextTextPassword);
        EditText passwordCheck = findViewById(R.id.editTextTextPassword2);
        ToggleButton student = findViewById(R.id.toggleButton);
        ToggleButton tutor = findViewById(R.id.toggleButton2);
        CheckBox checkBox = findViewById(R.id.checkBox);

        Button logIn = findViewById(R.id.button5);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                /** if required field are empty, signal to user */
                if(TextUtils.isEmpty(name.getText())){
                    name.setError("Name required");
                    fine = false;
                }
                if(TextUtils.isEmpty(email.getText())){
                    email.setError("Email required");
                    fine = false;
                }
                if(TextUtils.isEmpty(phoneNumber.getText())){
                    phoneNumber.setError("PhoneNumber required");
                    fine = false;
                }
                if(TextUtils.isEmpty(password.getText())){
                    password.setError("Password required");
                    fine = false;
                }
                if(TextUtils.isEmpty(passwordCheck.getText())){
                    passwordCheck.setError("please re-enter password");
                    fine = false;
                }
                if(TextUtils.isEmpty(userName.getText())){
                    userName.setError("please re-enter password");
                    fine = false;
                }
                if(!passwordCheck.getText().toString()
                        .equals(password.getText().toString())){
                    password.setError("Passwords do not match");
                    passwordCheck.setError("Passwords do not match");
                    fine = false;
                }
                if(!(student.isChecked() || tutor.isChecked())){
                    Toast.makeText(getApplicationContext(),
                            "Please Set a role",
                            Toast.LENGTH_SHORT)
                            .show();
                    fine = false;
                }
                if(!checkBox.isChecked()){
                    checkBox.setError("Must be checked to create account");
                    fine = false;
                }
                if(fine) {
                    User user = new User(userName.getText().toString(),
                            name.getText().toString(),
                            password.getText().toString(),
                            student.isChecked(),
                            tutor.isChecked(),
                            phoneNumber.getText().toString());
                    System.out.println(user.getUsername());
                }

            }
        });

    }
}
