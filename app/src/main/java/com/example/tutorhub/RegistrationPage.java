package com.example.tutorhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * To add:
 * valid phone number verification
 * valid email verification
 */
public class RegistrationPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference mDb;

    EditText userName;
    EditText name;
    EditText email;
    EditText educationalInstitution;
    EditText phoneNumber;
    EditText password;
    ToggleButton student;
    ToggleButton tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        mAuth = FirebaseAuth.getInstance();

        // variables
        userName = findViewById(R.id.usernameInput);
        name = findViewById(R.id.nameInput);
        email = findViewById(R.id.emailInput);
        educationalInstitution = findViewById(R.id.educ_inst);
        phoneNumber = findViewById(R.id.phonenr_input);
        password = findViewById(R.id.passwordInput);
        EditText passwordCheck = findViewById(R.id.password2Input);
        student = findViewById(R.id.toggle_student);
        tutor = findViewById(R.id.toggle_tutor);
        CheckBox checkBox = findViewById(R.id.checkBox_loc);



        Button logIn = findViewById(R.id.reg_button);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                String userEmail = email.getText().toString().trim();
                String userPassword= password.getText().toString().trim();
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
                if (fine) {
                    mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String email = writeNewUser();
                                Toast.makeText(RegistrationPage.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                String excString = task.getException().getMessage();
                                Toast.makeText(RegistrationPage.this, excString, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public String writeNewUser() {
        mDb = FirebaseDatabase.getInstance().getReference();
        User user;
        if (TextUtils.isEmpty(educationalInstitution.getText())){
            user = new User(userName.getText().toString(),
                    name.getText().toString(),
                    password.getText().toString(),
                    student.isChecked(),
                    tutor.isChecked(),
                    phoneNumber.getText().toString(),
                    email.getText().toString());
        }
        else {
            user = new User(userName.getText().toString(),
                    name.getText().toString(),
                    password.getText().toString(),
                    student.isChecked(),
                    tutor.isChecked(),
                    phoneNumber.getText().toString(),
                    educationalInstitution.getText().toString(),
                    email.getText().toString());
        }


        mDb.child("users").child(user.getEmail()).setValue(user);
        return user.getEmail();
    }
}
