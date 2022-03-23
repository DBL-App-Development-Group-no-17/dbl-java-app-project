package com.example.tutorhub;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * To add:
 * valid phone number verification
 * valid email verification
 */
public class RegistrationPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        // variables
        EditText userName = findViewById(R.id.usernameInput);
        EditText name = findViewById(R.id.nameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText educationalInstitution = findViewById(R.id.educ_inst);
        EditText phoneNumber = findViewById(R.id.phonenr_input);
        EditText password = findViewById(R.id.passwordInput);
        EditText passwordCheck = findViewById(R.id.password2Input);
        ToggleButton student = findViewById(R.id.toggle_student);
        ToggleButton tutor = findViewById(R.id.toggle_tutor);
        CheckBox checkBox = findViewById(R.id.checkBox_loc);

        Button logIn = findViewById(R.id.reg_button);
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
                    User user;
                    if (TextUtils.isEmpty(educationalInstitution.getText())){
                         user = new User(userName.getText().toString(),
                                name.getText().toString(),
                                password.getText().toString(),
                                student.isChecked(),
                                tutor.isChecked(),
                                phoneNumber.getText().toString());
                    }
                    else{
                        user = new User(userName.getText().toString(),
                                name.getText().toString(),
                                password.getText().toString(),
                                student.isChecked(),
                                tutor.isChecked(),
                                phoneNumber.getText().toString(),
                                educationalInstitution.getText().toString());

                    }
                }

            }
        });

    }
}
